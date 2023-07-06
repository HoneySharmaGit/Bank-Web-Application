package com.bankapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bankapp.entity.User;

public interface UserRepository extends JpaRepository<User, Integer> {

	User findByEmail(String email);

	User findByPhoneNumber(String phoneNumber);

	User findByEmailOrUserName(String email, String userName);

	User findByUserId(String userId);

}
