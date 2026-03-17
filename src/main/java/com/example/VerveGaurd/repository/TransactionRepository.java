package com.example.VerveGaurd.repository;

import com.example.VerveGaurd.dto.TransactionRequestDTO;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class TransactionRepository {
    private final JdbcTemplate jdbcTemplate;

    public TransactionRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void save(TransactionRequestDTO dto, boolean isFlagged, String reason) {
        String sql;
        sql = """
                INSERT INTO transactionRequests
                    (amount, cardNumber, ipAddress, merchantId, isFlagged, reason)
                    VALUES (?,?,?,?,?,?)
                """;

        jdbcTemplate.update(sql, dto.getAmount(),
                dto.getCardNumber(), dto.getMerchantId(), dto.getIpAddress(),
                isFlagged, reason);
    }


}
