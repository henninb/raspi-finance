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
  account_type CHAR(10) NOT NULL,
  active_status CHAR(1) NOT NULL,
  moniker CHAR(5),
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
  account_type CHAR(10),
  account_name_owner CHAR(40) NOT NULL,
  transaction_id INTEGER DEFAULT nextval('t_transaction_transaction_id_seq') NOT NULL,
  guid CHAR(70) NOT NULL,
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
--  account_type CHAR(10),
--  account_name_owner CHAR(40) NOT NULL,
--  guid CHAR(70) NOT NULL,
--  sha256 CHAR(70),
--  transaction_date TIMESTAMP NOT NULL,
--  description VARCHAR(75) NOT NULL,
--  category VARCHAR(50),
--  amount DECIMAL(12,2) NOT NULL,
--  cleared INTEGER,
--  notes VARCHAR(100)
--);

select conrelid::regclass AS table_from, conname, pg_get_constraintdef(c.oid) from pg_constraint c join pg_namespace n ON n.oid = c.connamespace where  contype in ('f', 'p','c','u') order by contype;
