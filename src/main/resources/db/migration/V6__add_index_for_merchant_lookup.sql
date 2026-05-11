-- Index to optimize merchant lookup by ID with blacklist status
-- Covers the common query pattern: findById + checking blacklisted status
CREATE INDEX idx_merchant_id_blacklist ON dbo.Merchant (Id, blacklisted, blacklistedAt)
GO
