@echo off

rem C:\Program Files (x86)\pgAdmin 4\v2\runtime

type aaaa_tables.sql aaab_tables.sql example.sql zzzy_tables.sql zzzz_tables.sql > master.sql

echo the windows client is touchy with command line arguments
psql -h 192.168.100.25 -p 5432 -d finance_db -U henninb < master.sql > log.txt 2>&1
rem psql -h localhost -p 5432 -d finance_db -U henninb < master.sql > log.txt 2>&1
rem psql finance_db henninb  < master.sql > log.txt 2>&1
rem del master.sql

pause
