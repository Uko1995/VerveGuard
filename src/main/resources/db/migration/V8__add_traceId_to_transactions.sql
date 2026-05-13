-- Add traceId column to TransactionRequests table for log correlation
ALTER TABLE dbo.TransactionRequests
ADD traceId NVARCHAR(50) NULL;

-- Create index for traceId lookups (correlating logs with transactions)
CREATE NONCLUSTERED INDEX idx_transaction_traceId
ON dbo.TransactionRequests(traceId)
INCLUDE (merchantId, isFlagged, status, createdAt);

