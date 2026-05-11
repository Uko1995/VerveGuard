
-- add index for faster queries for scheduler
CREATE INDEX  idx_blacklist ON dbo.Merchant (blacklisted, blacklistedAt)
GO