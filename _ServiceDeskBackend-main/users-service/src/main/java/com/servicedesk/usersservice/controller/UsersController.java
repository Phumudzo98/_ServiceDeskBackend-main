package com.servicedesk.usersservice.controller;

import com.servicedesk.usersservice.dao.*;
import com.servicedesk.usersservice.dao.company.UpdatePasswordRequest;
import com.servicedesk.usersservice.model.Account;
import com.servicedesk.usersservice.model.AgentAccount;
import com.servicedesk.usersservice.model.UserAccount;
import com.servicedesk.usersservice.model.company.Administrator;
import com.servicedesk.usersservice.repository.AgentAccountRepository;
import com.servicedesk.usersservice.repository.UserAccountRepository;
import com.servicedesk.usersservice.repository.company.AccountRepository;
import com.servicedesk.usersservice.repository.company.AdministratorRepository;
import com.servicedesk.usersservice.service.AuthService;
import com.servicedesk.usersservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Blob;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Validated
public class UsersController {

    private final UserService userService;
    private final AdministratorRepository administratorRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private  final AgentAccountRepository agentAccountRepository;
    private  final UserAccountRepository userAccountRepository;

    private final AuthService authService;

    @PostMapping(value = "/register-users/{companyId}")
    public ResponseEntity<AccountResponse> registerUsers(@PathVariable("companyId") String companyId,
                                             @RequestBody AccountRequest[] accountRequests){

        return ResponseEntity.ok(userService.registerUsers(accountRequests, companyId));
    }

    @GetMapping(value = "/get-agents/{companyId}")
    public ResponseEntity<List<AgentResponse>> getListOfAgents(@PathVariable("companyId") String companyId){

        List<AgentResponse> agents = userService.getListOfAgents(UUID.fromString(companyId));

        return ResponseEntity.ok(agents);
    }
    @GetMapping(value = "/all-users/{companyId}")
    public ResponseEntity<List<UserResponse>> getListOfUsers(@PathVariable("companyId") String companyId){

        List<UserResponse> Users = userService.getListOfUsers(UUID.fromString(companyId));

        return ResponseEntity.ok(Users);
    }
    @GetMapping(value = "/get-agent/{agentId}")
    public ResponseEntity<AgentResponse> getAgent(@PathVariable("agentId") String agentId){
        AgentResponse agent = userService.getAgent(UUID.fromString(agentId));
        return ResponseEntity.ok(agent);
    }

    @GetMapping(value = "/get-users/{companyId}")
    public ResponseEntity<List<UserResponse>> getAllUsers(@PathVariable("companyId") String companyId){
        List<UserResponse> users = userService.getAllUsers(companyId);
        return ResponseEntity.ok(users);
    }

    @PutMapping(value = "/reset-password/{accountId}/{role}")
    public ResponseEntity<?> updatePassword(@PathVariable("accountId") String accountId,
                                            @PathVariable("role") String role){

        boolean passwordUpdated = userService.updatePassword(accountId, role);

        if(passwordUpdated)
         return ResponseEntity.ok(new Response("Password reset Successful"));

        return  ResponseEntity.badRequest().body("Error! Something went wrong");
    }

    @GetMapping(value = "/get-user/{accountId}")
    public UserResponse getUser(@PathVariable("accountId") String accountId){
        return userService.getUser(UUID.fromString(accountId));
    }

    @PutMapping(value = "/change-password",consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> changePassword(@RequestBody ChangePassword changePassword){

        boolean passwordChanged = userService.changePassword(changePassword);

        if(passwordChanged)
            return ResponseEntity.ok(new Response("Password Changed Successful"));

        return  ResponseEntity.badRequest().body("Error! Something went wrong");
    }

    @GetMapping(value = "/get-users-data/{companyId}",produces = MediaType.APPLICATION_JSON_VALUE)
    public UserData getUsers(@PathVariable("companyId") String companyId){
        return userService.userData(UUID.fromString(companyId));
    }

    @PostMapping("/verifyOldPassword")
    public ResponseEntity<Object> verifyOldPassword(@RequestBody Account request) {
        String email = request.getEmail();
        String password = request.getPassword();
        System.out.println(email);
        System.out.println(password);

        Optional<AgentAccount> agentAccountOptional = agentAccountRepository.findByEmail(email);
 Optional<UserAccount> userAccountOptional =userAccountRepository.findByEmail(email);
        if (agentAccountOptional.isPresent()) {
            AgentAccount agentAccount = agentAccountOptional.get();
            String encodedPassword = agentAccount.getPassword();
            if (passwordEncoder.matches(password, encodedPassword)) {
                return ResponseEntity.ok(new Response("Old password verified"));
            } else {
                return ResponseEntity.badRequest().body(new Response("Old password is incorrect"));
            }
        } else if (userAccountOptional.isPresent()){
            UserAccount userAccount = userAccountOptional.get();
            String encodedPassword = userAccount.getPassword();
            if (passwordEncoder.matches(password, encodedPassword)) {
                return ResponseEntity.ok(new Response("Old password verified"));
            } else {
                return ResponseEntity.badRequest().body(new Response("Old password is incorrect"));
            }
        }else {
            return ResponseEntity.badRequest().body(new Response("User not found"));
        }

    }
    @PostMapping("/changePassword")
    public ResponseEntity<Object> changePassword(@RequestBody UpdatePasswordRequest changePasswordDTO) {
        String email = changePasswordDTO.getEmail();
        String newPassword = changePasswordDTO.getPassword();

        Optional<UserAccount> userAccountOptional = userAccountRepository.findByEmail(email);
        Optional<AgentAccount> agentAccountOptional = agentAccountRepository.findByEmail(email);

        if (userAccountOptional.isPresent()) {
            UserAccount userAccount = userAccountOptional.get();

            // Encode the new password
            String hashedPassword = passwordEncoder.encode(newPassword);
            userAccount.setPassword(hashedPassword);
            userAccountRepository.save(userAccount);

            return ResponseEntity.ok().body("{\"message\": \"Password changed successfully\"}");
        } else if (agentAccountOptional.isPresent()) {
            AgentAccount agentAccount = agentAccountOptional.get();

            // Encode the new password
            String hashedPassword = passwordEncoder.encode(newPassword);
            agentAccount.setPassword(hashedPassword);
            agentAccountRepository.save(agentAccount);

            return ResponseEntity.ok().body("{\"message\": \"Password changed successfully\"}");
        } else {
            return ResponseEntity.badRequest().body("{\"message\": \"User not found\"}");
        }
    }

    @PostMapping(value = "/changeAgentStatus")
    public ResponseEntity<?> updateAgentStatus(@RequestBody AgentStatusRequest request) {
        userService.updateAgentStatus(request.getEmail(), request.getStatusAgent());

        return ResponseEntity.ok().build();
    }
    @PostMapping(value = "/changeUserStatus")
    public ResponseEntity<?> updateUserStatus(@RequestBody AgentStatusRequest request) {
        userService.updateUserStatus(request.getEmail(),request.getStatusAgent());
        return ResponseEntity.ok().build();
    }
    @GetMapping("/displayProfileImage")
    public ResponseEntity<byte[]> displayProfileImage(@RequestParam("email") String email) {
        try {
            Optional<AgentAccount> agentAccountOpt = agentAccountRepository.findByEmail(email);
            Optional<UserAccount> userAccountOpt = userAccountRepository.findByEmail(email);

            if (userAccountOpt.isPresent()) {
                Blob profileImageBlob = userAccountOpt.get().getImage();
                if (profileImageBlob != null) {
                    int blobLength = (int) profileImageBlob.length();
                    byte[] profileImageBytes = profileImageBlob.getBytes(1, blobLength);
                    return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(profileImageBytes);
                } else {
                    // Handle case where no profile image is found for user
                    // You might want to return a default image or some message
                    return ResponseEntity.notFound().build();
                }
            } else if (agentAccountOpt.isPresent()) {
                Blob profileImageBlob = agentAccountOpt.get().getImage();
                if (profileImageBlob != null) {
                    int blobLength = (int) profileImageBlob.length();
                    byte[] profileImageBytes = profileImageBlob.getBytes(1, blobLength);
                    return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(profileImageBytes);
                } else {
                    // Handle case where no profile image is found for agent
                    // You might want to return a default image or some message
                    return ResponseEntity.notFound().build();
                }
            } else {
                // Handle case where neither user nor agent is found with the email
                return ResponseEntity.notFound().build();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }


    }

    @PostMapping("/updateProfile")
    public ResponseEntity<Object> updateProfile(@RequestParam("fullName") String fullName,
                                                @RequestParam("lastName") String lastName,
                                                @RequestParam("email") String email,
                                                @RequestParam("position") String position,
                                                @RequestParam(value = "file", required = false) MultipartFile file) {
        try {
            Blob profileImageBlob = null; // Initialize as null

            if (file != null && !file.isEmpty()) {
                // If file is provided, read bytes and create Blob
                byte[] bytes = file.getBytes();
                profileImageBlob = new javax.sql.rowset.serial.SerialBlob(bytes);
            }

            // Call the service method to update the profile
            ResponseEntity<Map<String, String>> responseEntity = userService.updateProfile(fullName, lastName, email,position, profileImageBlob);

            // Return the response entity from the service method
            return ResponseEntity.status(responseEntity.getStatusCode()).body(responseEntity.getBody());

        } catch (Exception e) {
            // Handle exceptions appropriately
            return ResponseEntity.badRequest().body("Failed to update profile: " + e.getMessage());
        }
    }


}