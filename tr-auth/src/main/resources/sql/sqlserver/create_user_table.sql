-- ######################################################################################################
-- # CREATE USER TABLES                                                                                 #
-- ######################################################################################################


-- ### USER TABLE #######################################################################################
IF OBJECT_ID(N'users', 'U') IS NOT NULL BEGIN
	DROP TABLE [users];
END

CREATE TABLE [users] (
	[pk_user_id] BINARY(16) PRIMARY KEY,
	[username] VARCHAR(32) UNIQUE NOT NULL,
	[password] CHARACTER(32) NOT NULL, -- MD5
	[forename] VARCHAR(64) NOT NULL,
	[surname] VARCHAR(64) NOT NULL
)