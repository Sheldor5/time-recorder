-- ######################################################################################################
-- # CREATE TABLES                                                                                      #
-- ######################################################################################################

IF OBJECT_ID(N'time-recorder', 'U') IS NOT NULL BEGIN
	DROP TABLE [time-recorder];
END

CREATE TABLE [time-recorder] (
	[pk_update_id] INT IDENTITY(0, 1) PRIMARY KEY,
	[timestamp] DATETIME DEFAULT CURRENT_TIMESTAMP,
	[version_major] INT NOT NULL,
	[version_minor] INT NOT NULL,
	[version_patch] INT NOT NULL,
	CONSTRAINT [uq_version] UNIQUE ([version_major], [version_minor], [version_patch])
)

IF OBJECT_ID(N'users', 'U') IS NOT NULL BEGIN
	DROP TABLE [users];
END

CREATE TABLE [users] (
	[pk_user_id] INT IDENTITY(1, 1) PRIMARY KEY,
	[username] VARCHAR(32) UNIQUE NOT NULL,
	[password] CHARACTER(32) NOT NULL,
	[forename] VARCHAR(64) NOT NULL,
	[surname] VARCHAR(64) NOT NULL
)

IF OBJECT_ID(N'records', 'U') IS NOT NULL BEGIN
	DROP TABLE [records];
END

CREATE TABLE [records] (
	[pk_record_id] INT IDENTITY(1, 1) PRIMARY KEY,
	[fk_user_id] INT FOREIGN KEY REFERENCES [users]([pk_user_id]) NOT NULL,
	[date] DATE NOT NULL,
	[time] TIME NOT NULL,
	[type] BIT NOT NULL
)