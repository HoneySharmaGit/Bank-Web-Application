package com.bankapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bankapp.entity.Address;

public interface AddressRepository extends JpaRepository<Address, Integer> {

	Address findByAddressId(int addressId);

}
