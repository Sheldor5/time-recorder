-- ######################################################################################################
-- # CREATE SYSTEM TABLES                                                                               #
-- ######################################################################################################


-- ### APPLICATION TABLE ################################################################################
IF OBJECT_ID(N'time-recorder', 'U') IS NOT NULL BEGIN
	DROP TABLE [time-recorder];
END

CREATE TABLE [time-recorder] (
	[pk_update_id] INT IDENTITY(1, 1) PRIMARY KEY,
	[timestamp] DATETIME DEFAULT CURRENT_TIMESTAMP,
	[version_major] INT NOT NULL,
	[version_minor] INT NOT NULL,
	[version_patch] INT NOT NULL,
	CONSTRAINT [uq_version] UNIQUE ([version_major], [version_minor], [version_patch])
)

-- ### RECORDS TABLE ####################################################################################
IF OBJECT_ID(N'records', 'U') IS NOT NULL BEGIN
	DROP TABLE [records];
END

CREATE TABLE [records] (
	[pk_record_id] INT IDENTITY(1, 1) PRIMARY KEY,
	[fk_user_id] INT NOT NULL,
	[date] DATE NOT NULL,
	[time] TIME NOT NULL,
	[type] BIT NOT NULL
)

-- ### USER MAPPING TABLE ###############################################################################
IF OBJECT_ID(N'user_mapping', 'U') IS NOT NULL BEGIN
  DROP TABLE [user_mapping];
END

CREATE TABLE [user_mapping] (
  [pk_user_id] INT IDENTITY(1, 1) PRIMARY KEY,
  [uuid] BINARY(16) UNIQUE NOT NULL
)

-- ### TABLE PROPERTIES #################################################################################
ALTER TABLE [records] ADD FOREIGN KEY ([fk_user_id]) REFERENCES [user_mapping]([pk_user_id]);

CREATE NONCLUSTERED INDEX [ix_records_date] ON [records] ([date]);