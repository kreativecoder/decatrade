package com.decagon.decatrade.repository;

import com.decagon.decatrade.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    Optional<Transaction> findByReference(String reference);

    Optional<Transaction> findByReferenceAndUserId(String reference, long userId);

    List<Transaction> findByUserId(long userId);

    List<Transaction> findByUserIdAndCreatedAtBetween(long userId, Date from, Date to);

    List<Transaction> findTop5ByUserIdOrderByCreatedAtDesc(long userId);
}
