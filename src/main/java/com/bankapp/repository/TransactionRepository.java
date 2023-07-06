package com.bankapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.bankapp.entity.Transaction;
import com.bankapp.entity.User;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Integer> {

	List<Transaction> findAllByUser(User user);

	List<Transaction> findAllByUserAndStatus(User user, String string);

	@Query("SELECT SUM(transaction.value) FROM Transaction transaction WHERE transaction.user=:user AND status='withdraw'")
	double findAllWithdrawnByUser(User user);

	@Query("SELECT SUM(transaction.value) FROM Transaction transaction WHERE transaction.user=:user AND status='deposit'")
	double findAllDepositByUser(User user);

}
