

CREATE TABLE merchant
(
    id             bigint IDENTITY (1, 1) NOT NULL,
    is_blacklisted bit NOT NULL,
    created_at     datetime,
    CONSTRAINT pk_merchant PRIMARY KEY (id)
)
    GO