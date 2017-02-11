-- ######################################################################################################
-- # DROP DATABASE                                                                                    #
-- ######################################################################################################
USE master
GO

SET NOCOUNT ON

IF EXISTS(SELECT * FROM sysdatabases WHERE name = N'TimeRecorder') BEGIN
	ALTER DATABASE [TimeRecorder] SET SINGLE_USER WITH ROLLBACK IMMEDIATE;
    DROP DATABASE [TimeRecorder];
END ELSE BEGIN
  PRINT N'Database does not exist';
END
GO