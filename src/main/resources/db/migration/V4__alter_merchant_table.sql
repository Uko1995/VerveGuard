-- add the new column
ALTER TABLE dbo.Merchant
ADD blacklisted  BIT NOT NULL default 0;
GO

-- copy data from old column to new column
UPDATE dbo.Merchant
SET blacklisted = isBlacklisted;
GO

-- Step 3: Drop the default constraint on isBlacklisted first
DECLARE @constraintName NVARCHAR(200)
SELECT @constraintName = dc.name
FROM sys.default_constraints dc
         JOIN sys.columns c ON dc.parent_object_id = c.object_id
    AND dc.parent_column_id = c.column_id
         JOIN sys.tables t ON c.object_id = t.object_id
WHERE t.name = 'Merchant'
  AND c.name = 'isBlacklisted';

IF @constraintName IS NOT NULL
BEGIN
EXEC('ALTER TABLE dbo.Merchant DROP CONSTRAINT ' + @constraintName);
END
GO

-- drop unwanted column
ALTER TABLE dbo.Merchant
DROP COLUMN isBlacklisted;
GO


