
CREATE TABLE dbo.Merchant (
    Id BIGINT NOT NULL  IDENTITY(1,1),
    createdAt DATETIME2 DEFAULT GETDATE(),
    isBlacklisted BIT default 0,
    PRIMARY KEY(Id)
);

