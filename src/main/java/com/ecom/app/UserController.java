package com.ecom.app;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class UserController {
	
	private final UserService userService;

	/*if you don't want to use the @RequiredArgsConstructor you can use a constructor{constructor injection}
	alternatively you can use @Autowired. 
	however, @RequiredArgsConstructor is generally better than field-level @Autowired */

    @GetMapping("/api/users")
    public ResponseEntity<List<User>> getAllUsers() {
    	return new ResponseEntity<>(userService.fetchAllUsers(), HttpStatus.OK);
    }
    @GetMapping("/api/{id}")
    public ResponseEntity<User> getUser(@PathVariable Long id) {

        return userService.fetchUser(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
    
    @PostMapping("/api/addUser")
    public String createUser(@RequestBody User user) {
        userService.addUser(user);
        return "User added Successfully"; 
    }
    
    @PutMapping("/api/updateUser/{id}")
    public ResponseEntity<String> updateUser(@PathVariable Long id, @RequestBody User updatedUser) {
    	
    	boolean updated = userService.updateUser(id, updatedUser);
    	if(updated) {
    		return ResponseEntity.ok("User updated successfully");
    	}
    	return ResponseEntity.notFound().build();
    	
    }
}
