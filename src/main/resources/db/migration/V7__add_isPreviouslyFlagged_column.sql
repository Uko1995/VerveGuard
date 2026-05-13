-- Add isPreviouslyFlagged column to Merchant table
ALTER TABLE dbo.Merchant
ADD isPreviouslyFlagged BIT DEFAULT 0 NOT NULL;

-- Add index for findByIdFresh repository method (covers Id lookups with key columns)
CREATE NONCLUSTERED INDEX idx_merchant_fresh_lookup
ON dbo.Merchant(Id)
INCLUDE (cardNumber, name, email, blacklisted, blacklistedAt, isPreviouslyFlagged);

GO

