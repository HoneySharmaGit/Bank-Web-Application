package com.bankapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bankapp.entity.Admin;
import com.bankapp.entity.StatusResponse;
import com.bankapp.service.AdminService;

@RestController
@RequestMapping("/admin")
@CrossOrigin(origins = { "*" })
public class AdminController {

	@Autowired
	private AdminService adminService;

	// register code for admin

	// code for login for user
	// requestbody -> email, password
	@PostMapping("/login")
	private ResponseEntity<?> adminLogin(@RequestBody(required = false) Admin admin) {
		try {
			return adminService.adminLogin(admin);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body((new StatusResponse("error", e.getMessage())));
		}
	}

	// code for getting admin details using adminId
	@GetMapping("/{adminId}")
	private ResponseEntity<?> getAdminDetails(@PathVariable(value = "adminId", required = false) int adminId) {
		try {
			return adminService.getAdminByAdminId(adminId);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body((new StatusResponse("error", e.getMessage())));
		}
	}

	// code for getting all the users list
	@GetMapping("/{adminId}/users")
	private ResponseEntity<?> allUsersList(@PathVariable(value = "adminId", required = false) int adminId) {
		try {
			return adminService.getAllUsersList(adminId);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body((new StatusResponse("error", e.getMessage())));
		}
	}

	// code for getting specific user details using userId(String)
	@GetMapping("/{adminId}/user/{userId}")
	private ResponseEntity<?> getUserById(@PathVariable(value = "adminId", required = false) int adminId,
			@PathVariable(value = "userId", required = false) String userId) {
		try {
			return adminService.getUserByUserId(adminId, userId);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body((new StatusResponse("error", e.getMessage())));
		}
	}

}
