package com.decagon.decatrade.repository;

import com.decagon.decatrade.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    Optional<Transaction> findByReference(String reference);

    Optional<Transaction> findByReferenceAndUserId(String reference, long userId);

    List<Transaction> findByUserId(long userId);
}
