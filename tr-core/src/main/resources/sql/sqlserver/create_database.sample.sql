USE master
GO

SET NOCOUNT ON

-- ######################################################################################################
-- # CREATE DATABASE                                                                                    #
-- ######################################################################################################

IF EXISTS(SELECT * FROM sysdatabases WHERE name = N'TimeRecorder') BEGIN
	ALTER DATABASE [TimeRecorder] SET SINGLE_USER WITH ROLLBACK IMMEDIATE;
    DROP DATABASE [TimeRecorder];
END
GO

CREATE DATABASE [TimeRecorder]
	ON PRIMARY (
	  Name = N'TR_DATA',
	  Filename='C:\Program Files\Microsoft SQL Server\MSSQL12.MSSQLSERVER\MSSQL\DATA\TR_DATA.mdf',
	  Size = 10MB,
	  Filegrowth = 10%
	)
	LOG ON (
	  Name = 'TR_LOG',
	  Filename='C:\Program Files\Microsoft SQL Server\MSSQL12.MSSQLSERVER\MSSQL\DATA\TR_LOG.ldf',
	  Size = 10MB,
	  Filegrowth = 10%
	)
GO

-- ######################################################################################################
-- # CREATE LOGINS, USERS AND GRANT PERMISSIONS                                                         #
-- ######################################################################################################

IF NOT EXISTS (SELECT * FROM sys.server_principals WHERE name = 'user') BEGIN
	CREATE LOGIN [user] WITH PASSWORD = 'pass'
END
GO

USE [TimeRecorder]
GO

IF NOT EXISTS (SELECT * FROM sys.database_principals WHERE name = N'user') BEGIN
	CREATE USER [user] FOR LOGIN [user];
END

EXEC sp_addrolemember N'db_owner', N'user'