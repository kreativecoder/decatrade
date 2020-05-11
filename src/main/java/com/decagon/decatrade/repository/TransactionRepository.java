package com.decagon.decatrade.repository;

import com.decagon.decatrade.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    Optional<Transaction> findByReference(String reference);

    Optional<Transaction> findByReferenceAndUserId(String reference, long userId);

    List<Transaction> findByUserId(long userId);

    List<Transaction> findByUserIdAndCreatedAtBetween(long userId, Date from, Date to);

    List<Transaction> findTop5ByUserIdOrderByCreatedAtDesc(long userId);

    @Query(value = "select symbol, sum(amount) as amount from transactions group by user_id, symbol, transaction_type having user_id = :userId and transaction_type = 'BUY'", nativeQuery = true)
    List<Map<String, BigDecimal>> getAmountPaidByUser(@Param("userId") long userId);
}
