package com.bankapp.service;

import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.List;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.bankapp.entity.Admin;
import com.bankapp.entity.StatusResponse;
import com.bankapp.entity.User;
import com.bankapp.exception.AdminNotFound;
import com.bankapp.exception.IncorrectPassword;
import com.bankapp.exception.UserNotFound;
import com.bankapp.repository.AdminRepository;
import com.bankapp.repository.UserRepository;

@Service
public class AdminService {

	@Autowired
	private AdminRepository adminRepo;

	@Autowired
	private UserRepository userRepo;

	public ResponseEntity<?> adminLogin(Admin admin) {
		if (admin == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new StatusResponse("error", "bad request"));
		}
		Admin savedAdmin = adminRepo.findByEmail(admin.getEmail());
		if (savedAdmin == null) {
			throw new AdminNotFound("admin not found");
		}
		// decrypt password
		Decoder decoder = Base64.getDecoder();
		byte[] decodedBytes = decoder.decode(savedAdmin.getPassword());
		String decryptedPass = new String(decodedBytes, StandardCharsets.UTF_8);
		System.out.println(savedAdmin.getPassword());
		if (decryptedPass.equals(admin.getPassword())) {
			return ResponseEntity.ok(savedAdmin);
		} else {
			throw new IncorrectPassword("incorrect password");
		}
	}

	public ResponseEntity<?> getAdminByAdminId(int adminId) {
		if (adminId <= 0) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new StatusResponse("error", "bad request"));
		}
		Admin admin = adminRepo.findByAdminId(adminId);
		if (admin != null) {
			Map<String, Object> map = new TreeMap<>();
			map.put("name", admin.getFirstName() + " " + admin.getLastName());
			map.put("email", admin.getEmail());
			map.put("phoneNumber", admin.getPhoneNumber());
			map.put("profile", admin.getProfile());
			return ResponseEntity.ok(map);
		} else {
			throw new AdminNotFound("admin not found");
		}
	}

	public ResponseEntity<?> getAllUsersList(int adminId) {
		if (adminId <= 0) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new StatusResponse("error", "bad request"));
		}
		Admin admin = adminRepo.findByAdminId(adminId);
		if (admin != null) {
			List<User> userList = userRepo.findAll();
			if (!userList.isEmpty()) {
				List<Map<String, Object>> list = new ArrayList<>();
				for (User user : userList) {
					Map<String, Object> map = new TreeMap<>();
					map.put("userId", user.getUserId());
					map.put("name", user.getFirstName() + " " + user.getLastName());
					map.put("email", user.getEmail());
					map.put("accountNumber", user.getAccountNumber());
					map.put("accountType", user.getAccountType());
					map.put("accountStatus", user.getAccountStatus());
					map.put("balance", user.getBalance());
					list.add(map);
				}
				return ResponseEntity.ok(list);
			} else {
				return ResponseEntity.ok(userList);
			}
		} else {
			throw new AdminNotFound("admin not found");
		}
	}

	public ResponseEntity<?> getUserByUserId(int adminId, String userId) {
		if (adminId <= 0 || userId == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new StatusResponse("error", "bad request"));
		}
		Admin admin = adminRepo.findByAdminId(adminId);
		if (admin != null) {
			User user = userRepo.findByUserId(userId);
			if (user != null) {
				Map<String, Object> map = new TreeMap<>();
				map.put("userId", user.getUserId());
				map.put("firstName", user.getFirstName());
				map.put("lastName", user.getLastName());
				map.put("email", user.getEmail());
				map.put("phoneNumber", user.getPhoneNumber());
				map.put("profile", user.getProfile());
				map.put("userName", user.getUserName());
				map.put("createdAt", user.getCreatedAt());
				map.put("gender", user.getGender());
				map.put("accountNumber", user.getAccountNumber());
				map.put("accountType", user.getAccountType());
				map.put("accountStatus", user.getAccountStatus());
				map.put("balance", user.getBalance());
				map.put("address", user.getAddress());

				return ResponseEntity.ok(map);
			} else {
				throw new UserNotFound("user not found");
			}
		} else {
			throw new AdminNotFound("admin not found");
		}
	}

}
