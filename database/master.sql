--set client_min_messages = warning;
--\d+ (shows sequence)
--epoch
--select extract(epoch from date_added) from t_transaction;
--select date_part('epoch', date_added) from t_transaction;
--SELECT EXTRACT(EPOCH FROM TIMESTAMP '2016-10-25T00:14:30.000');
--extract(epoch from date_trunc('month', current_timestamp)
--REVOKE CONNECT ON DATABASE finance_db FROM PUBLIC, henninb;

--TO_TIMESTAMP('1538438975')

DROP DATABASE IF EXISTS finance_db;
CREATE DATABASE finance_db;
GRANT ALL PRIVILEGES ON DATABASE finance_db TO henninb;
\connect finance_db;

CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

--create the SEQUENCE prior to the table.
--DROP SEQUENCE IF EXISTS t_account_account_id_seq CASCADE;
--to_timestamp(0)
CREATE SEQUENCE t_account_account_id_seq START WITH 1001;

DROP TABLE IF EXISTS t_account;
CREATE TABLE IF NOT EXISTS t_account(
  --account_id INTEGER DEFAULT nextval('t_account_account_id_seq') PRIMARY KEY NOT NULL,
  account_id INTEGER DEFAULT nextval('t_account_account_id_seq') NOT NULL,
  account_name_owner CHAR(40) NOT NULL,
  account_name CHAR(20), -- NULL for now
  account_owner CHAR(20), -- NULL for now
  account_type CHAR(6) NOT NULL,
  active_status CHAR(1) NOT NULL,
  moniker CHAR(4),
  totals DECIMAL(12,2) DEFAULT 0.0,
  totals_balanced DECIMAL(12,2) DEFAULT 0.0,
  date_closed TIMESTAMP DEFAULT TO_TIMESTAMP(0),
  date_updated TIMESTAMP DEFAULT TO_TIMESTAMP(0),
  date_added TIMESTAMP DEFAULT TO_TIMESTAMP(0)
);

--ALTER TABLE t_account ADD PRIMARY KEY (account_id);
--ALTER TABLE t_account ALTER COLUMN account_id set DEFAULT nextval('t_account_account_id_seq');
--ALTER TABLE t_account ADD DEFAULT nextval('t_account_account_id_seq') (account_id);

--create unique index account_id_idx on t_account(account_id);
CREATE UNIQUE INDEX account_name_owner_idx on t_account(account_name_owner);

CREATE OR REPLACE FUNCTION fn_upd_ts_account() RETURNS TRIGGER AS
$$
DECLARE
BEGIN
  RAISE NOTICE 'fn_upd_ts_account';
  NEW.date_updated := CURRENT_TIMESTAMP;
  RETURN NEW;
END;
$$ LANGUAGE PLPGSQL;

DROP TRIGGER IF EXISTS tr_upd_ts_account on t_account;
CREATE TRIGGER tr_upd_ts_account BEFORE UPDATE ON t_account FOR EACH ROW EXECUTE PROCEDURE fn_upd_ts_account();

CREATE OR REPLACE FUNCTION fn_ins_ts_account() RETURNS TRIGGER AS
$$
BEGIN
  RAISE NOTICE 'fn_ins_ts_account';
  NEW.date_added := CURRENT_TIMESTAMP;
  RETURN NEW;
END;
$$ LANGUAGE PLPGSQL;

DROP TRIGGER IF EXISTS tr_ins_ts_account on t_account;
CREATE TRIGGER tr_ins_ts_account BEFORE INSERT ON t_account FOR EACH ROW EXECUTE PROCEDURE fn_ins_ts_account();

--create the SEQUENCE prior to the table.
--DROP SEQUENCE IF EXISTS t_summary_summary_id_seq CASCADE;
CREATE SEQUENCE t_summary_summary_id_seq start with 1001;

DROP TABLE IF EXISTS t_summary;
CREATE TABLE IF NOT EXISTS t_summary (
  summary_id INTEGER DEFAULT nextval('t_summary_summary_id_seq') NOT NULL,
  --summary_id serial PRIMARY KEY,
  guid CHAR(70),
  account_name_owner CHAR(40) NOT NULL,
  totals DECIMAL(12,2) NOT NULL,
  totals_balanced DECIMAL(12,2) NOT NULL,
  date_updated TIMESTAMP,
  date_added TIMESTAMP
);

CREATE SEQUENCE t_category_category_id_seq start with 1001;

DROP TABLE IF EXISTS t_category;
CREATE TABLE IF NOT EXISTS t_category(
  category_id INTEGER DEFAULT nextval('t_category_category_id_seq') NOT NULL,
  category VARCHAR(50)
);

DROP TABLE IF EXISTS t_transaction_categories;
CREATE TABLE IF NOT EXISTS t_transaction_categories(
  category_id INTEGER NOT NULL,
  transaction_id INTEGER NOT NULL
);

--Actually nextval will advance sequence and return the new value
--SELECT NEXTVAL('t_summary_summary_id_seq');

-- ************************************** --
-- *** t_transaction table operations *** --
-- ************************************** --

--create the SEQUENCE prior to the table.
--DROP SEQUENCE IF EXISTS t_transaction_transaction_id_seq CASCADE;
CREATE SEQUENCE t_transaction_transaction_id_seq start with 1001;

DROP TABLE IF EXISTS t_transaction;
CREATE TABLE IF NOT EXISTS t_transaction (
  account_id INTEGER,
  account_type CHAR(6),
  account_name_owner CHAR(40) NOT NULL,
  transaction_id INTEGER DEFAULT nextval('t_transaction_transaction_id_seq') NOT NULL,
  guid CHAR(36) NOT NULL,
  sha256 CHAR(70),
  transaction_date DATE NOT NULL,
  description VARCHAR(75) NOT NULL,
  category VARCHAR(50),
  amount DECIMAL(12,2) NOT NULL DEFAULT 0.0,
  cleared INTEGER,
  reoccurring BOOLEAN DEFAULT FALSE,
  notes VARCHAR(100),
  date_updated TIMESTAMP DEFAULT TO_TIMESTAMP(0),
  date_added TIMESTAMP DEFAULT TO_TIMESTAMP(0)
  --CONSTRAINT t_transaction_pk PRIMARY KEY (guid)
  --CONSTRAINT t_transaction_unique UNIQUE (guid)
);

ALTER TABLE t_transaction ADD CONSTRAINT transaction_constraint UNIQUE (account_name_owner, transaction_date, description, category, amount, notes);

CREATE UNIQUE INDEX guid_idx ON t_transaction(guid);

CREATE OR REPLACE FUNCTION fn_ins_ts_transaction() RETURNS TRIGGER AS
$$
DECLARE
BEGIN
  RAISE NOTICE 'fn_ins_ts_transaction';
  NEW.date_added := CURRENT_TIMESTAMP;
  RETURN NEW;
END;
$$ LANGUAGE PLPGSQL;

DROP TRIGGER IF EXISTS tr_ins_ts_transactions on t_transaction;
CREATE TRIGGER tr_ins_ts_transactions BEFORE INSERT ON t_transaction FOR EACH ROW EXECUTE PROCEDURE fn_ins_ts_transaction();

CREATE OR REPLACE FUNCTION fn_upd_ts_transaction() RETURNS TRIGGER AS
$$
DECLARE
BEGIN
  RAISE NOTICE 'fn_upd_ts_transaction';
  NEW.date_updated := CURRENT_TIMESTAMP;
  RETURN NEW;
END;
$$ LANGUAGE PLPGSQL;

DROP TRIGGER IF EXISTS tr_upd_ts_transactions on t_transaction;
CREATE TRIGGER tr_upd_ts_transactions BEFORE UPDATE ON t_transaction FOR EACH ROW EXECUTE PROCEDURE fn_upd_ts_transaction();

--SELECT * FROM information_schema.constraint_table_usage WHERE table_name = 't_transaction';
--ALTER TABLE t_transaction DROP CONSTRAINT guid_idx;
--ALTER TABLE sample.public.employee DROP CONSTRAINT test_const
--DROP INDEX guid_idx;
--DROP TRIGGER tr_upd_ts_account  on t_account;
--DROP TRIGGER tr_upd_ts_transactions  on t_transaction;
--DROP TABLE IF EXISTS t_transaction_reoccur;

--CREATE TABLE IF NOT EXISTS t_transaction_reoccur (
--  account_id INTEGER,
--  account_type CHAR(6),
--  account_name_owner CHAR(40) NOT NULL,
--  guid CHAR(36) NOT NULL,
--  sha256 CHAR(70),
--  transaction_date TIMESTAMP NOT NULL,
--  description VARCHAR(75) NOT NULL,
--  category VARCHAR(50),
--  amount DECIMAL(12,2) NOT NULL,
--  cleared INTEGER,
--  notes VARCHAR(100)
--);

select conrelid::regclass AS table_from, conname, pg_get_constraintdef(c.oid) from pg_constraint c join pg_namespace n ON n.oid = c.connamespace where  contype in ('f', 'p','c','u') order by contype;


INSERT INTO t_category(category) VALUES('adjustment');
INSERT INTO t_category(category) VALUES('allina');
INSERT INTO t_category(category) VALUES('assessment');
INSERT INTO t_category(category) VALUES('atm');
INSERT INTO t_category(category) VALUES('automotive');
INSERT INTO t_category(category) VALUES('automotive_tabs');
INSERT INTO t_category(category) VALUES('bill_pay');
INSERT INTO t_category(category) VALUES('bonus');
INSERT INTO t_category(category) VALUES('brokerage');
INSERT INTO t_category(category) VALUES('canceled');
INSERT INTO t_category(category) VALUES('cash');
INSERT INTO t_category(category) VALUES('chargeback');
INSERT INTO t_category(category) VALUES('check');
INSERT INTO t_category(category) VALUES('checking');
INSERT INTO t_category(category) VALUES('checks');
INSERT INTO t_category(category) VALUES('church');
INSERT INTO t_category(category) VALUES('communication');
INSERT INTO t_category(category) VALUES('credit');
INSERT INTO t_category(category) VALUES('deposit');
INSERT INTO t_category(category) VALUES('direct_deposit');
INSERT INTO t_category(category) VALUES('discoverbank');
INSERT INTO t_category(category) VALUES('dividend');
INSERT INTO t_category(category) VALUES('donation');
INSERT INTO t_category(category) VALUES('drivers_license');
INSERT INTO t_category(category) VALUES('electronics');
INSERT INTO t_category(category) VALUES('entertainment');
INSERT INTO t_category(category) VALUES('federal_tax_refund');
INSERT INTO t_category(category) VALUES('fishing_license');
INSERT INTO t_category(category) VALUES('fuel');
INSERT INTO t_category(category) VALUES('garbage');
INSERT INTO t_category(category) VALUES('general_merchandise');
INSERT INTO t_category(category) VALUES('groceries');
INSERT INTO t_category(category) VALUES('home_improvement');
INSERT INTO t_category(category) VALUES('house_payment');
INSERT INTO t_category(category) VALUES('house_repairs');
INSERT INTO t_category(category) VALUES('ingdirect');
INSERT INTO t_category(category) VALUES('insulation');
INSERT INTO t_category(category) VALUES('insurance');
INSERT INTO t_category(category) VALUES('insurance_auto');
INSERT INTO t_category(category) VALUES('insurance_home');
INSERT INTO t_category(category) VALUES('interest');
INSERT INTO t_category(category) VALUES('ira');
INSERT INTO t_category(category) VALUES('irs');
INSERT INTO t_category(category) VALUES('license');
INSERT INTO t_category(category) VALUES('liquor');
INSERT INTO t_category(category) VALUES('lodging');
INSERT INTO t_category(category) VALUES('maintenance');
INSERT INTO t_category(category) VALUES('meat');
INSERT INTO t_category(category) VALUES('medical');
INSERT INTO t_category(category) VALUES('mn_tax_return');
INSERT INTO t_category(category) VALUES('none');
INSERT INTO t_category(category) VALUES('nurse_license');
INSERT INTO t_category(category) VALUES('oil_change');
INSERT INTO t_category(category) VALUES('online');
INSERT INTO t_category(category) VALUES('orthodontist');
INSERT INTO t_category(category) VALUES('paycheck');
INSERT INTO t_category(category) VALUES('payment');
INSERT INTO t_category(category) VALUES('paypal');
INSERT INTO t_category(category) VALUES('phone');
INSERT INTO t_category(category) VALUES('pictures');
INSERT INTO t_category(category) VALUES('plumbing');
INSERT INTO t_category(category) VALUES('postage');
INSERT INTO t_category(category) VALUES('property_taxes');
INSERT INTO t_category(category) VALUES('rebate');
INSERT INTO t_category(category) VALUES('redeemed');
INSERT INTO t_category(category) VALUES('refund');
INSERT INTO t_category(category) VALUES('restaurant');
INSERT INTO t_category(category) VALUES('return');
INSERT INTO t_category(category) VALUES('reversal');
INSERT INTO t_category(category) VALUES('rewards');
INSERT INTO t_category(category) VALUES('savings');
INSERT INTO t_category(category) VALUES('school');
INSERT INTO t_category(category) VALUES('small_balance_payment');
INSERT INTO t_category(category) VALUES('state_tax_refund');
INSERT INTO t_category(category) VALUES('taxes');
INSERT INTO t_category(category) VALUES('utilities');
INSERT INTO t_category(category) VALUES('vacation');
INSERT INTO t_category(category) VALUES('vehicle');
INSERT INTO t_category(category) VALUES('withdrawal');
INSERT INTO t_category(category) VALUES('work_expense');
INSERT INTO t_transaction(guid, transaction_date, description, category, amount, cleared, reoccurring, notes, date_updated, date_added, account_name_owner) VALUES('83e30475-fcfc-4f30-8395-0bb40e89b568',to_timestamp('1353456000'),'Initial Balance','','0.00','1','false','Account Opened',to_timestamp('1487300996'),to_timestamp('1487300996'),'amazon_store_brian');
INSERT INTO t_transaction(guid, transaction_date, description, category, amount, cleared, reoccurring, notes, date_updated, date_added, account_name_owner) VALUES('233b26e3-1aec-4d9a-a8e5-bf1e14f59a68',to_timestamp('1353542400'),'Amazon.com','online','30.99','1','false','',to_timestamp('1487300996'),to_timestamp('1487300996'),'amazon_store_brian');
INSERT INTO t_transaction(guid, transaction_date, description, category, amount, cleared, reoccurring, notes, date_updated, date_added, account_name_owner) VALUES('e11fc0e5-6be6-4389-8f96-871393261e01',to_timestamp('1353801600'),'Amazon.com','online','0.99','1','false','mp3',to_timestamp('1487300996'),to_timestamp('1487300996'),'amazon_store_brian');
INSERT INTO t_transaction(guid, transaction_date, description, category, amount, cleared, reoccurring, notes, date_updated, date_added, account_name_owner) VALUES('9b9aea08-0dc2-4720-b20c-00b0df6af8ce',to_timestamp('1353801600'),'Amazon.com','online','-0.99','1','false','reversal',to_timestamp('1487300996'),to_timestamp('1487300996'),'amazon_store_brian');
INSERT INTO t_transaction(guid, transaction_date, description, category, amount, cleared, reoccurring, notes, date_updated, date_added, account_name_owner) VALUES('ce678c51-5a87-45b2-87df-208a7356a6a3',to_timestamp('1354147200'),'Payment','bcu','-30.99','1','false','Confirmation Number: 446132529',to_timestamp('1487300996'),to_timestamp('1487300996'),'amazon_store_brian');
INSERT INTO t_transaction(guid, transaction_date, description, category, amount, cleared, reoccurring, notes, date_updated, date_added, account_name_owner) VALUES('38739c5b-e2c6-41cc-82c2-d41f39a33f9a',to_timestamp('1513156823'),'Last Updated','','0.00','-3','false','Statement Closing Date - TBD',to_timestamp('1513212329'),to_timestamp('1513212329'),'amazon_store_brian');

INSERT INTO t_transaction(guid, transaction_date, description, category, amount, cleared, reoccurring, notes, date_updated, date_added, account_name_owner) VALUES('4cd48585-8d29-4926-bef6-2f54cda860b0',to_timestamp('1489881600'),'Initial Balance','','13.68','1','false','Account Opened',to_timestamp('1489979141'),to_timestamp('1489979141'),'amazongift_brian');
INSERT INTO t_transaction(guid, transaction_date, description, category, amount, cleared, reoccurring, notes, date_updated, date_added, account_name_owner) VALUES('bd515232-ef71-4c2b-ab79-08ad60afecd3',to_timestamp('1489968000'),'Amazon Gift Card','online','3.32','1','false','',to_timestamp('1490011928'),to_timestamp('1490011928'),'amazongift_brian');
INSERT INTO t_transaction(guid, transaction_date, description, category, amount, cleared, reoccurring, notes, date_updated, date_added, account_name_owner) VALUES('b965fee5-fa70-44ad-b0e4-41542dd56520',to_timestamp('1490140800'),'Amazon Gift Card','online','30.00','1','false','',to_timestamp('1490238126'),to_timestamp('1490238126'),'amazongift_brian');
INSERT INTO t_transaction(guid, transaction_date, description, category, amount, cleared, reoccurring, notes, date_updated, date_added, account_name_owner) VALUES('d1711371-7270-49a3-8062-e143d8a21ed2',to_timestamp('1490140800'),'Amazon Gift Card','online','13.91','1','false','',to_timestamp('1490238157'),to_timestamp('1490238157'),'amazongift_brian');
INSERT INTO t_transaction(guid, transaction_date, description, category, amount, cleared, reoccurring, notes, date_updated, date_added, account_name_owner) VALUES('ef1ded0c-fce0-4eba-bf80-9512f1b8b6f3',to_timestamp('1490313600'),'Amazon Gift Card','online','3.95','1','false','',to_timestamp('1490361725'),to_timestamp('1490361725'),'amazongift_brian');
INSERT INTO t_transaction(guid, transaction_date, description, category, amount, cleared, reoccurring, notes, date_updated, date_added, account_name_owner) VALUES('3b011e62-1a75-42d7-9cae-9e69d28db603',to_timestamp('1490400000'),'Amazon Gift Card','online','4.00','1','false','',to_timestamp('1490453087'),to_timestamp('1490453087'),'amazongift_brian');
INSERT INTO t_transaction(guid, transaction_date, description, category, amount, cleared, reoccurring, notes, date_updated, date_added, account_name_owner) VALUES('5405b2bb-8d19-420d-a569-f4301e3d87b1',to_timestamp('1491264000'),'Amazon Gift Card','online','0.99','1','false','',to_timestamp('1491438860'),to_timestamp('1491438860'),'amazongift_brian');


SELECT count(*) into void_record_i from t_transaction WHERE amount='0.00' AND cleared=1 AND description = 'void' AND notes='';
RAISE notice 'Number of void records deleted: %', void_record_i;

SELECT count(*) into none_record_i from t_transaction WHERE amount='0.00' AND cleared=1 AND description = 'none' AND notes='';
RAISE notice 'Number of none records deleted: %', none_record_i;

DELETE FROM t_transaction WHERE amount='0.00' AND cleared=1 AND description = 'void' AND notes='';
DELETE FROM t_transaction WHERE amount='0.00' AND cleared=1 AND description = 'none' AND notes='';

--UPDATE t_transaction set amount = (amount * -1.0) where account_type = 'credit';

UPDATE t_transaction SET account_id = x.account_id, account_type = x.account_type FROM (SELECT account_id, account_name_owner, account_type FROM t_account) x WHERE t_transaction.account_name_owner = x.account_name_owner;

SELECT account_name_owner, SUM(amount) AS credits FROM t_transaction WHERE account_type = 'credit' GROUP BY account_name_owner ORDER BY account_name_owner;
SELECT account_name_owner, SUM(amount) AS totals FROM t_transaction GROUP BY account_name_owner ORDER BY account_name_owner;

SELECT A.debits AS DEBITS, B.credits AS CREDITS FROM
      ( SELECT SUM(amount) AS debits FROM t_transaction WHERE account_type = 'debit' ) A,
      ( SELECT SUM(amount) AS credits FROM t_transaction WHERE account_type = 'credit' ) B;

RAISE NOTICE 'Not sure';
UPDATE t_account SET totals = x.totals FROM (SELECT (A.debits - B.credits) AS totals FROM  
      ( SELECT SUM(amount) AS debits FROM t_transaction WHERE account_type = 'debit' ) A,
      ( SELECT SUM(amount) AS credits FROM t_transaction WHERE account_type = 'credit' ) B) x WHERE t_account.account_name_owner = 'grand.total_dummy'; 

RAISE NOTICE 'Grand Total';
SELECT (A.debits - B.credits) AS TOTALS FROM  
      ( SELECT SUM(amount) AS debits FROM t_transaction WHERE account_type = 'debit' ) A,
      ( SELECT SUM(amount) AS credits FROM t_transaction WHERE account_type = 'credit' ) B; 

RAISE NOTICE 'Looking for dupliate GUIDs';
SELECT guid FROM t_transaction GROUP BY 1 HAVING COUNT(*) > 1;

CREATE OR REPLACE FUNCTION fn_ins_summary() RETURNS void AS $$
  INSERT INTO t_summary(summary_id, guid, account_name_owner, totals, totals_balanced, date_updated, date_added)
  (SELECT nextval('t_summary_summary_id_seq'), C.uuid AS guid, A.account_name_owner, A.totals AS totals, B.totals_balanced AS totals_balanced, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM
    ( SELECT account_name_owner, SUM(amount) AS totals FROM t_transaction GROUP BY account_name_owner ) A,
    ( SELECT account_name_owner, SUM(amount) AS totals_balanced FROM t_transaction WHERE cleared=1 GROUP BY account_name_owner ) B, 
    ( SELECT uuid_generate_v4() AS uuid ) C
   WHERE A.account_name_owner = B.account_name_owner);
   UPDATE t_account SET totals = x.totals FROM (SELECT account_name_owner, SUM(amount) AS totals FROM t_transaction GROUP BY account_name_owner) x WHERE t_account.account_name_owner = x.account_name_owner;
   UPDATE t_account SET totals_balanced = x.totals_balanced FROM (SELECT account_name_owner, SUM(amount) AS totals_balanced FROM t_transaction WHERE cleared = 1 GROUP BY account_name_owner) x WHERE t_account.account_name_owner = x.account_name_owner;
   UPDATE t_account SET totals = x.totals FROM (SELECT (A.debits - B.credits) AS totals FROM
      ( SELECT SUM(amount) AS debits FROM t_transaction WHERE account_type = 'debit' ) A,
      ( SELECT SUM(amount) AS credits FROM t_transaction WHERE account_type = 'credit' ) B) x WHERE t_account.account_name_owner = 'grand.total_dummy'; 

$$ LANGUAGE SQL;

RAISE NOTICE 'Populate Summary';
SELECT NULL AS 'Populate Summary';
SELECT fn_ins_summary();

RAISE NOTICE 'Summary by account';
SELECT NULL AS 'Summary by account';
SELECT * FROM t_summary WHERE guid IN (SELECT guid FROM t_summary ORDER BY date_added DESC LIMIT 1) ORDER BY account_name_owner;

RAISE NOTICE 'Two or more spaces in the description';
SELECT NULL AS 'Two or more spaces in the description';
SELECT description FROM t_transaction WHERE description like '%  %';

RAISE NOTICE 'Two or more spaces in the notes';
SELECT NULL AS 'Two or more spaces in the notes';
SELECT notes FROM t_transaction WHERE notes like '%  %';

RAISE NOTICE 'Two or more spaces in the category';
SELECT NULL AS 'Two or more spaces in the category';
SELECT category FROM t_transaction WHERE category like '%  %';

\copy (SELECT * FROM t_transaction) TO finance_db.csv WITH (FORMAT csv, HEADER true)
