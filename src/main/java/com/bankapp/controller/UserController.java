package com.bankapp.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bankapp.entity.Address;
import com.bankapp.entity.StatusResponse;
import com.bankapp.entity.User;
import com.bankapp.service.UserService;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = { "*" })
public class UserController {

	@Autowired
	private UserService userService;

	@PostMapping("/register")
	private ResponseEntity<?> registerAsUser(@RequestBody(required = false) User user) {
		try {
			return userService.registerAsUser(user);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body((new StatusResponse("error", e.getMessage())));
		}
	}

	@PostMapping("/login")
	private ResponseEntity<?> userLogin(@RequestBody(required = false) User user) {
		try {
			return userService.userLogin(user);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body((new StatusResponse("error", e.getMessage())));
		}
	}

	@PostMapping("/sendotp")
	public ResponseEntity<?> sendOtpToUser(@RequestBody(required = false) Map<String, Object> map) {
		if (map.get("email") != null) {
			String email = (String) map.get("email");
			return userService.sendOtpOnEmail(email);
		} else if ((map.get("phoneNumber") != null)) {
			String phoneNumber = (String) map.get("phoneNumber");
			return userService.sendOtpOnNumber(phoneNumber);
		} else {
			return ResponseEntity.badRequest().body((new StatusResponse("error", "bad request")));
		}
	}

	@GetMapping("/{userId}")
	public ResponseEntity<?> getUser(@PathVariable(required = false) String userId) {
		try {
			return userService.getUserByUserId(userId);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body((new StatusResponse("error", e.getMessage())));
		}
	}

	@DeleteMapping("/{userId}/delete")
	public ResponseEntity<?> deleteUser(@PathVariable(required = false) String userId) {
		try {
			return userService.deleteUserByUserId(userId);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body((new StatusResponse("error", e.getMessage())));
		}
	}

	@PutMapping("/{userId}/edit")
	public ResponseEntity<?> updateUser(@PathVariable(value = "userId", required = false) String userId,
			@RequestBody(required = false) User editedUser) {
		try {
			return userService.updateUser(userId, editedUser);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body((new StatusResponse("error", e.getMessage())));
		}
	}

	@PostMapping("/{userId}/address")
	public ResponseEntity<?> addAddress(@PathVariable(value = "userId", required = false) String userId,
			@RequestBody(required = false) Address address) {
		try {
			return userService.addAddressOfUser(userId, address);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body((new StatusResponse("error", e.getMessage())));
		}
	}

	@PutMapping("/address/{addressId}/edit")
	public ResponseEntity<?> editAddress(@PathVariable(value = "addressId", required = false) int addressId,
			@RequestBody(required = false) Address editedAddress) {
		try {
			return userService.editAddressOfUser(addressId, editedAddress);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body((new StatusResponse("error", e.getMessage())));
		}
	}

	@DeleteMapping("/address/{addressId}/delete")
	public ResponseEntity<?> deleteAddress(@PathVariable(value = "addressId", required = false) int addressId) {
		try {
			return userService.deleteAddressOfUser(addressId);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body((new StatusResponse("error", e.getMessage())));
		}
	}

	// under production
	@PostMapping("/{userId}/withdraw")
	public ResponseEntity<?> withdrawMoney(@PathVariable(value = "userId", required = false) String userId,
			@RequestBody(required = false) Map<String, Object> map) {
		if (map.get("value") != null) {
			try {
				double value = (Double) map.get("value");
				String uniqueString = (String) map.get("uniqueString");
				return userService.withdrawMoneyFromAccount(userId, value, uniqueString);
			} catch (Exception e) {
				return ResponseEntity.badRequest().body((new StatusResponse("error", e.getMessage())));
			}
		} else {
			return ResponseEntity.badRequest().body((new StatusResponse("error", "bad request")));
		}
	}

	// under production
	@PostMapping("/{userId}/deposit")
	public ResponseEntity<?> depositMoney(@PathVariable(value = "userId", required = false) String userId,
			@RequestBody(required = false) Map<String, Object> map) {
		if (map.get("value") != null) {
			try {
				double value = (Double) map.get("value");
				return userService.depositMoneyInAccount(userId, value);
			} catch (Exception e) {
				return ResponseEntity.badRequest().body(new StatusResponse("error", e.getMessage()));
			}
		} else {
			return ResponseEntity.badRequest().body(new StatusResponse("error", "bad request"));
		}
	}

	@GetMapping("/{userId}/transactions")
	public ResponseEntity<?> getTransactions(@PathVariable(value = "uesrId", required = false) String userId) {
		try {
			return userService.getAllTransactionsOfUser(userId);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(new StatusResponse("error", e.getMessage()));
		}
	}

	@PostMapping("/{userId}/generatepin")
	public ResponseEntity<?> generatePin(@PathVariable(value = "uesrId", required = false) String userId,
			@RequestBody(required = false) Map<String, Object> map) {
		try {
			if (map.get("uniqueString") != null) {
				String uniqueString = (String) map.get("uniqueString");
				return userService.generateNewPin(userId, uniqueString);
			} else {
				return ResponseEntity.badRequest().body(new StatusResponse("error", "bad request"));
			}
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(new StatusResponse("error", e.getMessage()));
		}
	}

}
