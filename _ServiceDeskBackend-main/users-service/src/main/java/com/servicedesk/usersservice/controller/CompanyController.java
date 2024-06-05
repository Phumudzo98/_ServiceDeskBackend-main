package com.servicedesk.usersservice.controller;

import com.servicedesk.usersservice.dao.Response;
import com.servicedesk.usersservice.dao.company.*;
import com.servicedesk.usersservice.model.AgentAccount;
import com.servicedesk.usersservice.model.UserAccount;
import com.servicedesk.usersservice.model.company.Administrator;
import com.servicedesk.usersservice.repository.AgentAccountRepository;
import com.servicedesk.usersservice.repository.UserAccountRepository;
import com.servicedesk.usersservice.repository.company.AdministratorRepository;
import com.servicedesk.usersservice.service.AuthService;
import com.servicedesk.usersservice.service.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.*;

@RequiredArgsConstructor
@RequestMapping("/api/company")
@RestController
public class CompanyController {

    private final CompanyService companyService;
    private final Random random = new Random();
    private final BCryptPasswordEncoder passwordEncoder;
    private final AuthService authService;
    private final AdministratorRepository administratorRepository;
    private  final UserAccountRepository userAccountRepository;
    private  final AgentAccountRepository agentAccountRepository;
    @GetMapping(value = "/get-company/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CompanyResponse> getCompanyInformation(@PathVariable("id") String id) {
        CompanyResponse companyResponse = companyService.getCompany(UUID.fromString(id));
        return ResponseEntity.ok(companyResponse);
    }

    @PostMapping(value = "/registerAdministrator/{companyId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> registerAdmin(@PathVariable("companyId") String companyId, @RequestBody SignupRequest signupRequest) {
        signupRequest.setPassword("Password@" + random.nextInt(9000) + 100);
        if (companyService.registerAdministrator(signupRequest, companyId)) {
            return ResponseEntity.ok(new Response("Successfully Registered " + signupRequest.getFullName()
                    + " as Administrator"));
        }
        return ResponseEntity.badRequest().body("Error! Administrator already exist");
    }

    @GetMapping(value = "/get-registered-admins/{companyId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public List<AdministratorResponse> getCompanyRegisteredAdmins(@PathVariable("companyId") String companyId) {
        return companyService.getCompanyAdmins(UUID.fromString(companyId));
    }

    @PutMapping(value = "/update-password/{role}")
    public ResponseEntity<?> changePassword(@PathVariable("role") String role, @RequestBody UpdatePasswordRequest updatePasswordRequest) {
        if (companyService.changePassword(role, updatePasswordRequest))
            return ResponseEntity.ok(new Response("Password change successful"));
        return ResponseEntity.badRequest().body("Error! Failed to change password");
    }

    @PostMapping("/verifyOldPassword")
    public ResponseEntity<Object> verifyOldPassword(@RequestBody Administrator passwordRequest) {
        String email = passwordRequest.getEmail();
        String password = passwordRequest.getPassword();
        Optional<Administrator> administratorOptional = administratorRepository.findByEmail(email);

        if (administratorOptional.isPresent()) {
            Administrator administrator = administratorOptional.get();
            String encodedPassword = administrator.getPassword();
            if (passwordEncoder.matches(password, encodedPassword)) {
                return ResponseEntity.ok(new Response("Old password verified"));
            } else {
                return ResponseEntity.badRequest().body(new Response("Old password is incorrect"));
            }
        } else {
            return ResponseEntity.badRequest().body(new Response("User not found"));
        }
    }

    @PostMapping("/changePassword")
    public ResponseEntity<Object> changePassword(@RequestBody Administrator changePasswordDTO) {
        String email = changePasswordDTO.getEmail();
        String newPassword = changePasswordDTO.getPassword();

        Optional<Administrator> administratorOptional = administratorRepository.findByEmail(email);
        if (administratorOptional.isEmpty()) {
            return ResponseEntity.badRequest().body("{\"message\": \"User not found\"}");
        }

        Administrator administrator = administratorOptional.get();

        // Encode the new password
        String hashedPassword = passwordEncoder.encode(newPassword);
        administrator.setPassword(hashedPassword);
        administratorRepository.save(administrator);
        authService.setContentForUpdatePasswordNotififaction(administratorOptional .get());
        return ResponseEntity.ok().body("{\"message\": \"Password changed successfully\"}");
    }

    @PostMapping(value = "/logout/{email}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> logoutUser(@PathVariable String email) {
        try {
            // Call logoutUser method from AuthService with the email parameter
            authService.logoutUser(email);
            return ResponseEntity.ok("User status updated to offline");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error updating user status: " + e.getMessage());
        }
    }

    @GetMapping("/displayProfileImage")
    public ResponseEntity<byte[]> displayProfileImage(@RequestParam("email") String email) {
        try {
            Optional<Administrator> administrator = administratorRepository.findByEmail(email);

            // Ensure the user is not null
            if (!administrator.isPresent()) {
                return ResponseEntity.notFound().build();
            }

            Blob profileImageBlob = administrator.get().getImage();
            byte[] profileImageBytes;

            if (profileImageBlob != null) {
                int blobLength = (int) profileImageBlob.length();
                profileImageBytes = profileImageBlob.getBytes(1, blobLength);
            } else {
                // Handle case where no profile image is found
                // You might want to return a default image or some message
                return ResponseEntity.notFound().build();
            }

            return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(profileImageBytes);
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
            Optional<Administrator> administratorOptional = administratorRepository.findByEmail(email);
            Blob profileImageBlob = null; // Initialize as null

            if (file != null && !file.isEmpty()) {
                // If file is provided, read bytes and create Blob
                byte[] bytes = file.getBytes();
                profileImageBlob = new javax.sql.rowset.serial.SerialBlob(bytes);
            }

            // Call the service method to update the profile
            ResponseEntity<Map<String, String>> responseEntity = companyService.updateProfile(fullName, lastName, email, position,profileImageBlob);
             authService.setContentForUpdateProfileNotififaction(administratorOptional.get());
            // Return the response entity from the service method
            return ResponseEntity.status(responseEntity.getStatusCode()).body(responseEntity.getBody());

        } catch (Exception e) {
            // Handle exceptions appropriately
            return ResponseEntity.badRequest().body("Failed to update profile: " + e.getMessage());
        }
    }

}