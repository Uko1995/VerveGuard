package com.example.VerveGaurd.repository;

import com.example.VerveGaurd.dto.BlacklistedMerchantsResponse;
import com.example.VerveGaurd.dto.Status;
import com.example.VerveGaurd.dto.TransactionRequestDTO;
import com.example.VerveGaurd.util.EncryptionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TransactionRepository {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    private EncryptionUtil encryptionUtil;

    public TransactionRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void save(TransactionRequestDTO dto, boolean isFlagged, String reason, String ipAddress) {
        String sql;
        sql = """
                INSERT INTO transactionRequests
                    (amount, cardNumber, ipAddress, merchantId, isFlagged, reason, status)
                    VALUES (?,?,?,?,?,?,?)
                """;

        jdbcTemplate.update(sql, dto.getAmount(),
                encryptionUtil.encrypt(dto.getCardNumber()), ipAddress, dto.getMerchantId(),
                isFlagged, reason, isFlagged ? "FLAGGED" : "APPROVED");
    }

    public List<BlacklistedMerchantsResponse> findAllFlaggedWithMerchantDetails() {
        String sql = """
            SELECT 
                m.name,
                m.email,
                t.reason,
                t.isFlagged,
                t.status
            FROM transactionRequests t
            JOIN Merchant m ON t.merchantId = m.id
            WHERE t.isFlagged = 1
        """;

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            BlacklistedMerchantsResponse response = new BlacklistedMerchantsResponse();
            response.setName(rs.getString("name"));
            response.setEmail(rs.getString("email"));
            response.setReason(rs.getString("reason"));
            response.setIsFlagged(rs.getBoolean("isFlagged"));
            response.setStatus(Status.valueOf(rs.getString("status")));
            return response;
        });


    }
}
