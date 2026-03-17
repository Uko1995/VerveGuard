IF NOT EXISTS (SELECT name FROM sys.databases WHERE name = 'mydb')
BEGIN
    CREATE DATABASE mydb;
END
GO