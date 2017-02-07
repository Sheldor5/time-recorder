-- ######################################################################################################
-- # CREATE SYSTEM DATABASE                                                                             #
-- ######################################################################################################
USE master
GO

SET NOCOUNT ON

IF EXISTS(SELECT * FROM sysdatabases WHERE name = N'TimeRecorder') BEGIN
	PRINT N'Database already exists';
END ELSE BEGIN
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
		);
END
GO