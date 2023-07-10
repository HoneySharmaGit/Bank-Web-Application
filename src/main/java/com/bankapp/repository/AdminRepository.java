package com.bankapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bankapp.entity.Admin;

public interface AdminRepository extends JpaRepository<Admin, Integer> {

	Admin findByEmail(String email);

	Admin findByAdminId(int adminId);

}
