package com.servicedesk.usersservice.controller;

import com.servicedesk.usersservice.dao.LoginRequest;
import com.servicedesk.usersservice.dao.ResetPassword;
import com.servicedesk.usersservice.dao.Response;
import com.servicedesk.usersservice.dao.UserResponse;
import com.servicedesk.usersservice.dao.company.SignupRequest;
import com.servicedesk.usersservice.dao.company.forgotPassword;
import com.servicedesk.usersservice.model.AgentAccount;
import com.servicedesk.usersservice.model.UserAccount;
import com.servicedesk.usersservice.model.company.Administrator;
import com.servicedesk.usersservice.repository.AgentAccountRepository;
import com.servicedesk.usersservice.repository.UserAccountRepository;
import com.servicedesk.usersservice.repository.company.AdministratorRepository;
import com.servicedesk.usersservice.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Validated
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final AuthService authService;
    private final AdministratorRepository administratorRepository;
    private  final UserAccountRepository userAccountRepository;
    private  final AgentAccountRepository agentAccountRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        // Check if the email belongs to an AgentAccount
        AgentAccount agent = authService.getAgentByEmail(loginRequest.getEmail());
        UserAccount user =authService.getUserByEmail(loginRequest.getEmail());
        if (agent == null && user==null) {
            // If email does not belong to an AgentAccount, perform authentication
            return performAuthentication(loginRequest);
        }

        if (agent == null) {
            return userAuth(loginRequest);
        }else
            return agentAuth(loginRequest);

    }
  private ResponseEntity<?> agentAuth(LoginRequest loginRequest){

            // Fetch agent details
            AgentAccount agent = authService.getAgentByEmail(loginRequest.getEmail());
            if (agent == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
            }else {
                // Authenticate user credentials
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken
                        (loginRequest.getEmail(), loginRequest.getPassword()));
            }

            // Check if the user is active or disabled
            if ("Disabled".equalsIgnoreCase(agent.getStatusAgent())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied. User is disabled.");
            }

      // Update user status to "online" upon successful login
      authService.loginUser(loginRequest);

      // Generate JWT token and send it in the response
      String token = authService.generateToken(loginRequest.getEmail());
      return ResponseEntity.ok(new Response(token));
  }
    private ResponseEntity<?> userAuth(LoginRequest loginRequest){
        UserAccount user =authService.getUserByEmail(loginRequest.getEmail());
        //fetch user details

        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }else {
            // Authenticate user credentials
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken
                    (loginRequest.getEmail(), loginRequest.getPassword()));
        }
        // Check if the user is active or disabled
        if ("Disabled".equalsIgnoreCase(user.getStatusAgent())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied. User is disabled.");
        }

        // Update user status to "online" upon successful login
        authService.loginUser(loginRequest);

        // Generate JWT token and send it in the response
        String token = authService.generateToken(loginRequest.getEmail());
        return ResponseEntity.ok(new Response(token));
    }

    private ResponseEntity<?> performAuthentication(LoginRequest loginRequest) {
        try {
            // Authenticate user credentials
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken
                    (loginRequest.getEmail(), loginRequest.getPassword()));

            // Update user status to "online" upon successful login
            authService.loginUser(loginRequest);

            // Generate JWT token and send it in the response
            String token = authService.generateToken(loginRequest.getEmail());
            return ResponseEntity.ok(new Response(token));
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred");
        }
    }


    @PostMapping(value = "/signup", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> signup(@RequestBody SignupRequest signupRequest) {
        if (authService.signup(signupRequest)) {
            return ResponseEntity.ok(new Response("Successfully Registered Account"));
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error! Account already exists");
    }

    @PostMapping("/forgotPassword")
    public ResponseEntity<Map<String, String>> forgotPasswordProcess(@RequestBody forgotPassword usersSign) {
        Optional<UserAccount> existingUser = userAccountRepository.findByEmail(usersSign.getEmail());
        Optional<Administrator> administrator = administratorRepository.findByEmail(usersSign.getEmail());
        Optional<AgentAccount> agentAccount = agentAccountRepository.findByEmail(usersSign.getEmail());

        Map<String, String> response = new HashMap<>();

        if (existingUser.isPresent()) {
            // User exists, send email for password reset
            authService.sendEmail(existingUser.get());
            response.put("message", "Email sent successfully");
            return ResponseEntity.ok(response);
        } else if (administrator.isPresent()) {
            authService.sendEmail(administrator.get());
            response.put("message", "Email sent successfully");
            return ResponseEntity.ok(response);
        } else if (agentAccount.isPresent()) {
            authService.sendEmail(agentAccount.get());
            response.put("message", "Email sent successfully");
            return ResponseEntity.ok(response);
        } else {
            // User does not exist, return an error response
            response.put("message", "Email not found");
            return ResponseEntity.badRequest().body(response);
        }
    }


    @PostMapping("/resetPassword")
    public ResponseEntity<String> resetPassword(@RequestBody ResetPassword resetPassword) {
        String responseMessage = authService.resetPassword(resetPassword);
        return ResponseEntity.ok(responseMessage);
    }

}
