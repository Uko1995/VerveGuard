
CREATE TABLE dbo.Merchant (
    Id BIGINT NOT NULL  IDENTITY(1,1),
    cardNumber NVARCHAR(40) NOT NULL,
    name NVARCHAR(30) NOT NULL,
    email NVARCHAR(20) NOT NULL,
    createdAt DATETIME2 DEFAULT GETDATE(),
    isBlacklisted BIT default 0,
    blacklistedAt DATETIME2 DEFAULT GETDATE(),
    PRIMARY KEY(Id)
);

