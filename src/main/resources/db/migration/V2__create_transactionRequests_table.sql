CREATE TABLE dbo.TransactionRequests (
    Id BIGINT NOT NULL IDENTITY(1,1) ,
    amount decimal(19, 2),
    cardNumber NVARCHAR(16),
    ipAddress NVARCHAR(45),
    isFlagged BIT default 0,
    reason NVARCHAR(255),
    merchantId BIGINT NOT NULL,
    createdAt DATETIME2 DEFAULT GETDATE(),
    PRIMARY KEY(Id),
    CONSTRAINT fk_merchant FOREIGN KEY (merchantId) REFERENCES dbo.Merchant(Id),


);