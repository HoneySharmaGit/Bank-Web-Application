package com.bankapp.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(name = "user_id")
	private String userId;

	@Column(name = "first_name")
	private String firstName;

	@Column(name = "last_name")
	private String lastName;

	private String email;

	private String password;

	@Column(name = "phone_number")
	private String phoneNumber;

	private String gender;

	@Column(name = "atm_number")
	private int atmNumber;

	@Column(name = "atm_pin_modified_at")
	private String atmPinModifiedAt;

	@Column(name = "unique_string")
	private String uniqueString;

	private double balance = 0.0;

	@Column(name = "username")
	private String userName;

	@Column(name = "account_number")
	private String accountNumber;

	@Column(name = "account_type")
	private String accountType;

	@Column(name = "account_status")
	private String accountStatus;

	@OneToOne(mappedBy = "userId")
	private Address address;

	@Column(name = "created_at")
	private String createdAt;

	private int otp;

	@Column(name = "email_verified")
	private String emailVerified;

	@Column(name = "number_verified")
	private String numberVerified;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getUniqueString() {
		return uniqueString;
	}

	public void setUniqueString(String uniqueString) {
		this.uniqueString = uniqueString;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public int getAtmNumber() {
		return atmNumber;
	}

	public void setAtmNumber(int atmNumber) {
		this.atmNumber = atmNumber;
	}

	public String getAtmPinModifiedAt() {
		return atmPinModifiedAt;
	}

	public void setAtmPinModifiedAt(String atmPinModifiedAt) {
		this.atmPinModifiedAt = atmPinModifiedAt;
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public String getAccountType() {
		return accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	public String getAccountStatus() {
		return accountStatus;
	}

	public void setAccountStatus(String accountStatus) {
		this.accountStatus = accountStatus;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

	public int getOtp() {
		return otp;
	}

	public void setOtp(int otp) {
		this.otp = otp;
	}

	public String getEmailVerified() {
		return emailVerified;
	}

	public void setEmailVerified(String emailVerified) {
		this.emailVerified = emailVerified;
	}

	public String getNumberVerified() {
		return numberVerified;
	}

	public void setNumberVerified(String numberVerified) {
		this.numberVerified = numberVerified;
	}
}
