--set client_min_messages = warning;

--DROP DATABASE finance_db;

DROP TABLE IF EXISTS finance_db
CREATE DATABASE finance_db;

DROP TABLE t_transaction;
DROP TABLE t_transaction_reoccur;
DROP TABLE t_account;
DROP TABLE t_summary;

DROP SEQUENCE t_account_account_id_seq CASCADE;
CREATE SEQUENCE t_account_account_id_seq START WITH 1001; 

CREATE TABLE t_account(
  --account_id INTEGER DEFAULT nextval('t_account_account_id_seq') PRIMARY KEY NOT NULL,
  account_id INTEGER DEFAULT nextval('t_account_account_id_seq') NOT NULL,
  --account_id INTEGER NOT NULL,
  account_name_owner CHAR(40) NOT NULL,
  account_type CHAR(10) NOT NULL,
  active_status CHAR(1) NOT NULL,
  moniker CHAR(5),
  totals DECIMAL(12,2),
  totals_balanced DECIMAL(12,2),
  date_closed INTEGER,
  date_updated TIMESTAMP,
  date_added TIMESTAMP
);

--create unique index account_id_idx on t_account(account_id);
CREATE UNIQUE INDEX account_name_owner_idx on t_account(account_name_owner);

--ALTER TABLE t_account ADD PRIMARY KEY (account_id);

--ALTER TABLE t_account ALTER COLUMN account_id set DEFAULT nextval('t_account_account_id_seq');
--ALTER TABLE t_account ADD DEFAULT nextval('t_account_account_id_seq') (account_id);

CREATE OR REPLACE FUNCTION fn_upd_ts_account() RETURNS TRIGGER AS 
$$
BEGIN
  NEW.date_updated := CURRENT_TIMESTAMP;
   --OLD.date_updated := CURRENT_TIMESTAMP;
  RETURN NEW;
END;
$$ LANGUAGE PLPGSQL;


CREATE OR REPLACE FUNCTION fn_upd_ts_transaction() RETURNS TRIGGER AS 
$$
BEGIN
  NEW.date_updated := CURRENT_TIMESTAMP;
   --OLD.date_updated := CURRENT_TIMESTAMP;
  RETURN NEW;
END;
$$ LANGUAGE PLPGSQL;


DROP SEQUENCE t_summary_summary_id_seq CASCADE;
CREATE SEQUENCE t_summary_summary_id_seq start with 1;

CREATE TABLE IF NOT EXISTS t_summary (
  summary_id serial PRIMARY KEY,
  guid CHAR(70),
  account_name_owner CHAR(40) NOT NULL,
  totals DECIMAL(12,2) NOT NULL,
  totals_balanced DECIMAL(12,2) NOT NULL,
  date_updated TIMESTAMP,
  date_added TIMESTAMP
);

--Actually nextval will advance sequence and return the new value
select nextval('t_summary_summary_id_seq');

--CREATE extension "uuid-ossp";
--SELECT uuid_generate_v4();

--SELECT guid FROM t_summary ORDER BY date_added DESC LIMIT 1;

--WITH vars AS (SELECT uuid_generate_v4() AS uuid) select vars.uuid;

DROP SEQUENCE t_transaction_transaction_id_seq CASCADE;
CREATE SEQUENCE t_transaction_transaction_id_seq start with 1001;

--http://www.w3resource.com/PostgreSQL/unique.php
CREATE TABLE IF NOT EXISTS  t_transaction (
  --account_id INTEGER REFERENCES stores( store_id ),
  account_id INTEGER,
  account_type CHAR(10),
  account_name_owner CHAR(40) NOT NULL,
  transaction_id INTEGER DEFAULT nextval('t_transaction_transaction_id_seq') NOT NULL,
  guid CHAR(70) NOT NULL,
  sha256 CHAR(70),
  transaction_date TIMESTAMP NOT NULL,
  description VARCHAR(75) NOT NULL,
  category VARCHAR(50),
  amount DECIMAL(12,2) NOT NULL,
  cleared INTEGER,
  notes VARCHAR(100),
  date_updated TIMESTAMP,
  date_added TIMESTAMP
  --CONSTRAINT t_transaction_pk PRIMARY KEY (guid)
  --CONSTRAINT t_transaction_unique UNIQUE (guid)
);

--SELECT * FROM information_schema.constraint_table_usage WHERE table_name = 't_transaction';

--ALTER TABLE t_transaction DROP CONSTRAINT guid_idx;
--ALTER TABLE sample.public.employee DROP CONSTRAINT test_const

CREATE UNIQUE INDEX guid_idx ON t_transaction(guid);
--DROP INDEX guid_idx;

--DROP TRIGGER tr_upd_ts_account  on t_account;
--DROP TRIGGER tr_upd_ts_transactions  on t_transaction;

CREATE TRIGGER tr_upd_ts_transactions
BEFORE UPDATE ON t_transaction FOR EACH ROW EXECUTE PROCEDURE fn_upd_ts_transaction();

CREATE TABLE IF NOT EXISTS t_transaction_reoccur (
  account_id INTEGER,
  account_type CHAR(10),
  account_name_owner CHAR(40) NOT NULL,
  guid CHAR(70) NOT NULL,
  sha256 CHAR(70),
  transaction_date TIMESTAMP NOT NULL,
  description VARCHAR(75) NOT NULL,
  category VARCHAR(50),
  amount DECIMAL(12,2) NOT NULL,
  cleared INTEGER,
  notes VARCHAR(100)
);

CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
