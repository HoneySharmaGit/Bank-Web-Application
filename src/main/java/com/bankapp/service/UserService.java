package com.bankapp.service;

import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Base64.Encoder;
import java.util.List;
import java.util.Random;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bankapp.entity.User;
import com.bankapp.exception.AddressNotFound;
import com.bankapp.exception.BadRequest;
import com.bankapp.exception.BankAccountExists;
import com.bankapp.exception.IncorrectPassword;
import com.bankapp.exception.UserExists;
import com.bankapp.exception.UserNotFound;
import com.bankapp.helper.DateGenerator;
import com.bankapp.helper.MailHelper;
import com.bankapp.helper.SmsHelper;
import com.bankapp.entity.Address;
import com.bankapp.entity.StatusResponse;
import com.bankapp.repository.AddressRepository;
import com.bankapp.repository.TransactionRepository;
import com.bankapp.repository.UserRepository;
import com.bankapp.entity.Transaction;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private DateGenerator dateGenerator;

	@Autowired
	private MailHelper mailHelper;

	@Autowired
	private SmsHelper smsHelper;

	@Autowired
	private AddressRepository addressRepo;

	@Autowired
	private TransactionRepository transactionRepo;

	public ResponseEntity<?> registerAsUser(User user) {
		if (user == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new StatusResponse("error", "bad request"));
		}
		User userbyemail = userRepo.findByEmail(user.getEmail());
		User userbynumber = userRepo.findByPhoneNumber(user.getPhoneNumber());
		if (userbyemail != null) {
			throw new UserExists("email already exists");
		} else if (userbynumber != null) {
			throw new UserExists("phone number already exists");
		} else {
			user.setUserId("user" + System.currentTimeMillis());
			user.setCreatedAt(dateGenerator.getCurrentTime());
			// encrypt password before saving
			Encoder encoder = Base64.getEncoder();
			String encodedPassword = encoder.encodeToString(user.getPassword().getBytes());
			user.setPassword(encodedPassword);
			User savedUser = userRepo.save(user);
			Map<String, Object> map = new TreeMap<>();
			map.put("userId", savedUser.getUserId());
			map.put("email", savedUser.getEmail());
			return ResponseEntity.ok(map);
		}
	}

	public ResponseEntity<?> userLogin(User user) {
		if (user == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new StatusResponse("error", "bad request"));
		}
		User savedUser = userRepo.findByEmailOrUserName(user.getEmail(), user.getUserName());
		if (savedUser == null) {
			throw new UserNotFound("user not found");
		}
		// decrypt password
		Decoder decoder = Base64.getDecoder();
		byte[] decodedBytes = decoder.decode(savedUser.getPassword());
		String decodedPass = decodedBytes.toString();

		if (decodedPass.equals(user.getPassword())) {
			return ResponseEntity.ok(savedUser);
		} else {
			throw new IncorrectPassword("incorrect password");
		}
	}

	public ResponseEntity<?> sendOtpOnEmail(String email) {
		if (email == null) {
			throw new BadRequest("bad request");
		}

		int otp = mailHelper.randomOtpForMail();
		System.out.println("otp is --> " + otp);
		mailHelper.sendHtmlMail(email, "OTP for Login", "html code to be written");

		User savedUser = userRepo.findByEmail(email);
		if (savedUser != null) {
			savedUser.setOtp(otp);
			userRepo.save(savedUser);
		} else {
			User user = new User();
			user.setEmail(email);
			user.setUserId("user" + System.currentTimeMillis());
			user.setCreatedAt(dateGenerator.getCurrentTime());
			user.setEmailVerified("verified");
			userRepo.save(user);
		}
		return ResponseEntity.ok(new StatusResponse("success", "otp sent on email"));
	}

	public ResponseEntity<?> sendOtpOnNumber(String phoneNumber) {
		if (phoneNumber == null) {
			throw new BadRequest("bad request");
		}

		int otp = smsHelper.randomOtpForNumber();
		System.out.println("otp is --> " + otp);
		smsHelper.sendSms(phoneNumber, "OTP for Login is: " + otp);

		User savedUser = userRepo.findByPhoneNumber(phoneNumber);
		if (savedUser != null) {
			savedUser.setOtp(otp);
			userRepo.save(savedUser);
		} else {
			User user = new User();
			user.setPhoneNumber(phoneNumber);
			user.setUserId("user" + System.currentTimeMillis());
			user.setCreatedAt(dateGenerator.getCurrentTime());
			user.setPhoneNumber("verified");
			userRepo.save(user);
		}
		return ResponseEntity.ok(new StatusResponse("success", "otp sent on number"));
	}

	public ResponseEntity<?> updateUser(String userId, User editedUser) {
		if (userId == null || editedUser == null) {
			throw new BadRequest("invalid request");
		}
		User savedUser = userRepo.findByUserId(userId);
		if (savedUser != null) {
			if (editedUser.getFirstName() != null && !editedUser.getFirstName().equals(savedUser.getFirstName())) {
				savedUser.setFirstName(editedUser.getFirstName());
			}
			if (editedUser.getLastName() != null && !editedUser.getLastName().equals(savedUser.getLastName())) {
				savedUser.setLastName(editedUser.getLastName());
			}
			if (editedUser.getEmail() != null && !editedUser.getEmail().equals(savedUser.getEmail())) {
				savedUser.setEmail(editedUser.getEmail());
			}
			if (editedUser.getPhoneNumber() != null
					&& !editedUser.getPhoneNumber().equals(savedUser.getPhoneNumber())) {
				savedUser.setPhoneNumber(editedUser.getPhoneNumber());
			}
			if (editedUser.getGender() != null && !editedUser.getGender().equals(savedUser.getGender())) {
				savedUser.setGender(editedUser.getGender());
			}
			return ResponseEntity.ok(userRepo.save(savedUser));
		} else {
			throw new UserNotFound("user not found");
		}
	}

	public ResponseEntity<?> getUserByUserId(String userId) {
		if (userId == null) {
			throw new BadRequest("invalid request");
		}
		User savedUser = userRepo.findByUserId(userId);
		if (savedUser != null) {
			return ResponseEntity.ok(savedUser);
		} else {
			throw new UserNotFound("user not found");
		}
	}

	public ResponseEntity<?> deleteUserByUserId(String userId) {
		if (userId == null) {
			throw new BadRequest("invalid request");
		}
		User savedUser = userRepo.findByUserId(userId);
		if (savedUser != null) {
			userRepo.delete(savedUser);
			return ResponseEntity.ok(new StatusResponse("success", "user deleted"));
		} else {
			throw new UserNotFound("user not found");
		}
	}

	public ResponseEntity<?> addAddressOfUser(String userId, Address address) {
		if (userId == null || address == null) {
			throw new BadRequest("invalid request");
		}
		User user = userRepo.findByUserId(userId);
		if (user != null) {
			address.setUserId(user);
			Address savedAddress = addressRepo.save(address);

			user.setAddress(address);
			userRepo.save(user);
			return ResponseEntity.ok(savedAddress);
		} else {
			throw new UserNotFound("user not found");
		}
	}

	public ResponseEntity<?> editAddressOfUser(int addressId, Address editedAddress) {
		if (addressId == 0 || editedAddress == null) {
			throw new BadRequest("invalid request");
		}
		Address originalAddress = addressRepo.findByAddressId(addressId);
		if (originalAddress != null) {
			if (editedAddress.getHouseNo() != null
					&& !editedAddress.getHouseNo().equals(originalAddress.getHouseNo())) {
				originalAddress.setHouseNo(editedAddress.getHouseNo());
			}
			if (editedAddress.getStreet() != null && !editedAddress.getStreet().equals(originalAddress.getStreet())) {
				originalAddress.setStreet(editedAddress.getStreet());
			}
			if (editedAddress.getCity() != null && !editedAddress.getCity().equals(originalAddress.getCity())) {
				originalAddress.setCity(editedAddress.getCity());
			}
			if (editedAddress.getState() != null && !editedAddress.getState().equals(originalAddress.getState())) {
				originalAddress.setState(editedAddress.getState());
			}
			if (editedAddress.getCountry() != null
					&& !editedAddress.getCountry().equals(originalAddress.getCountry())) {
				originalAddress.setCountry(editedAddress.getCountry());
			}
			if (editedAddress.getPincode() != null
					&& !editedAddress.getPincode().equals(originalAddress.getPincode())) {
				originalAddress.setPincode(editedAddress.getPincode());
			}
			return ResponseEntity.ok(addressRepo.save(originalAddress));
		} else {
			throw new AddressNotFound("address not found");
		}
	}

	@Transactional
	public ResponseEntity<?> deleteAddressOfUser(int addressId) {
		if (addressId == 0) {
			throw new BadRequest("invalid request");
		}
		Address savedAddress = addressRepo.findByAddressId(addressId);
		if (savedAddress != null) {
			User user = savedAddress.getUserId();
			user.setAddress(null);
			userRepo.save(user);

			savedAddress.setUserId(null);
			Address newSavedAddress = addressRepo.save(savedAddress);
			addressRepo.delete(newSavedAddress);
			return ResponseEntity.ok(new StatusResponse("success", "address deleted"));
		} else {
			throw new AddressNotFound("address not found");
		}
	}

	public ResponseEntity<?> withdrawMoneyFromAccount(String userId, double value, String uniqueString) {
		if (value <= 0) {
			return ResponseEntity.badRequest().body(new StatusResponse("error", "bad request"));
		}
		User user = userRepo.findByUserId(userId);
		if (user != null) {
			if (!user.getUniqueString().equals(uniqueString)) {
				return ResponseEntity.badRequest().body(new StatusResponse("error", "invalid uniqueString"));
			}
			Transaction transaction = new Transaction();
			transaction.setTransactionId("txn" + System.currentTimeMillis() + userId);
			transaction.setStatus("withdraw");
			transaction.setHappendAt(dateGenerator.getCurrentTime());
			transaction.setValue(value);
			transaction.setUser(user);
			Transaction savedTransaction = transactionRepo.save(transaction);

			double withdrawnMoney = transactionRepo.findAllWithdrawnByUser(user);
			double depositedMoney = transactionRepo.findAllDepositByUser(user);
			double balance = depositedMoney - withdrawnMoney;
			user.setBalance(balance);
			userRepo.save(user);

			return ResponseEntity.ok(savedTransaction);
		} else {
			throw new UserNotFound("user not found");
		}
	}

	public ResponseEntity<?> depositMoneyInAccount(String userId, double value) {
		if (value <= 0) {
			return ResponseEntity.badRequest().body(new StatusResponse("error", "bad request"));
		}
		User user = userRepo.findByUserId(userId);
		if (user != null) {
			Transaction transaction = new Transaction();
			transaction.setTransactionId("txn" + System.currentTimeMillis() + userId);
			transaction.setStatus("deposit");
			transaction.setHappendAt(dateGenerator.getCurrentTime());
			transaction.setValue(value);
			transaction.setUser(user);
			Transaction savedTransaction = transactionRepo.save(transaction);

			double withdrawnMoney = transactionRepo.findAllWithdrawnByUser(user);
			double depositedMoney = transactionRepo.findAllDepositByUser(user);
			double balance = depositedMoney - withdrawnMoney;
			user.setBalance(balance);
			userRepo.save(user);

			return ResponseEntity.ok(savedTransaction);
		} else {
			throw new UserNotFound("user not found");
		}
	}

	public ResponseEntity<?> getAllTransactionsOfUser(String userId) {
		if (userId == null) {
			return ResponseEntity.badRequest().body(new StatusResponse("error", "bad request"));
		}
		User user = userRepo.findByUserId(userId);
		if (user != null) {
			List<Transaction> allTransactions = transactionRepo.findAllByUser(user);
			return ResponseEntity.ok(allTransactions);
		} else {
			throw new UserNotFound("user not found");
		}
	}

	public ResponseEntity<?> generateNewPin(String userId, String uniqueString) {
		if (userId == null) {
			return ResponseEntity.badRequest().body(new StatusResponse("error", "bad request"));
		}
		User user = userRepo.findByUserId(userId);
		if (user != null) {
			if (!user.getUniqueString().equals(uniqueString)) {
				return ResponseEntity.badRequest().body(new StatusResponse("error", "invalid uniqueString"));
			}
			Random random = new Random();
			int newPin = random.nextInt(9999);
			user.setAtmNumber(newPin);
			user.setAtmPinModifiedAt(dateGenerator.getCurrentTime());
			userRepo.save(user);
			return ResponseEntity.ok(newPin);
		} else {
			throw new UserNotFound("user not found");
		}
	}

	public ResponseEntity<?> requestForBankAccount(String userId, Map<String, Object> map) {
		if (userId == null) {
			return ResponseEntity.badRequest().body(new StatusResponse("error", "bad request"));
		}
		User user = userRepo.findByUserId(userId);
		if (user != null) {
			if (user.getAccountNumber() != null) {
				throw new BankAccountExists("bank account already exists");
			}
			user.setAccountNumber(String.valueOf(System.currentTimeMillis()) + user.getId());
			user.setAccountStatus("pending");
			user.setAccountType((String) map.get("accountType"));
			Random random = new Random();
			int atmPin = random.nextInt(9999);
			user.setAtmNumber(atmPin);
			user.setUniqueString(user.getId() + String.valueOf(System.currentTimeMillis()) + user.getId());
			User savedUser = userRepo.save(user);

			Map<String, Object> userMap = new TreeMap<>();
			userMap.put("accountNumber", savedUser.getAccountNumber());
			userMap.put("accountStatus", savedUser.getAccountStatus());
			userMap.put("accountType", savedUser.getAccountType());
			userMap.put("atmNumber", savedUser.getAtmNumber());
			userMap.put("uniqueString", savedUser.getUniqueString());
			return ResponseEntity.ok(userMap);
		} else {
			throw new UserNotFound("user not found");
		}
	}

}
