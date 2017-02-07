-- ######################################################################################################
-- # CREATE LOGINS, USERS AND GRANT PERMISSIONS                                                         #
-- ######################################################################################################
USE master
GO

SET NOCOUNT ON

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