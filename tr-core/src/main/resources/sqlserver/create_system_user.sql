-- ######################################################################################################
-- # CREATE LOGINS, USERS AND GRANT PERMISSIONS                                                         #
-- ######################################################################################################
USE master
GO

SET NOCOUNT ON

IF NOT EXISTS (SELECT * FROM sys.server_principals WHERE name = 'userMapping') BEGIN
	CREATE LOGIN [userMapping] WITH PASSWORD = 'pass'
END
GO

USE [TimeRecorder]
GO

IF NOT EXISTS (SELECT * FROM sys.database_principals WHERE name = N'userMapping') BEGIN
	CREATE USER [userMapping] FOR LOGIN [userMapping];
END

EXEC sp_addrolemember N'db_owner', N'userMapping'