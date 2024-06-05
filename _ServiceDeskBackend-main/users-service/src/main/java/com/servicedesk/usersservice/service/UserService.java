package com.servicedesk.usersservice.service;

import com.servicedesk.usersservice.dao.*;
import com.servicedesk.usersservice.model.AgentAccount;
import com.servicedesk.usersservice.model.UserAccount;
import com.servicedesk.usersservice.model.company.Administrator;
import com.servicedesk.usersservice.repository.AgentAccountRepository;
import com.servicedesk.usersservice.repository.UserAccountRepository;
import com.servicedesk.usersservice.repository.company.AdministratorRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Blob;
import java.time.LocalDate;
import java.util.*;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {


    private final UserAccountRepository userAccountRepository;
    private final AgentAccountRepository agentAccountRepository;
    private final PasswordEncoder passwordEncoder;
    private final SendEmailService sendEmailService;
    private final AdministratorRepository administratorRepository;
    private final Random random = new Random();
    private final CompanyService companyService;

    public AccountResponse registerUsers(AccountRequest[] accountRequests,String companyId){
         int successful=0,failed=0,users=0,agents=0;
        List<String> failedAccount = new ArrayList<>();

        for(AccountRequest accountRequest:accountRequests){
            if(accountRequest.getRole().equals("User")) {
                if (userAccountRepository.findByEmail(accountRequest.getEmail()).isPresent()) {
                    failed += 1;
                    failedAccount.add(userAccountRepository.findByEmail(accountRequest.getEmail()).get().getEmail());

                } else {
                    String password = "Password@"+random.nextInt(9000)+1000;
                    UserAccount userAccount = new UserAccount();
                    userAccount.setEmail(accountRequest.getEmail());
                    userAccount.setFullName(accountRequest.getFullName());
                    userAccount.setContactNumber(accountRequest.getContactNumber());
                    userAccount.setLastName(accountRequest.getLastName());
                    userAccount.setPosition(accountRequest.getPosition());
                    userAccount.setPassword(passwordEncoder.encode(password));
                    userAccount.setCompanyId(UUID.fromString(companyId));
                    userAccount.setExpired(false);
                    userAccount.setStatus("Offline");
                    userAccount.setStatusAgent("Active");
                    // Set the default profile picture for the user
                    Blob defaultProfilePicture = companyService.loadDefaultProfilePictureBlob();
                    userAccount.setImage(defaultProfilePicture);
                    successful += 1;
                    users+=1;
                    userAccountRepository.save(userAccount);
                    String content = this.setContent(accountRequest.getFullName(), accountRequest.getEmail(), password);
                    boolean isSent = sendEmailService.sendEmail(accountRequest.getEmail(), "Account Created",content);
                    if(isSent)
                        System.out.println("Email Sent");
                }
            }else {
                if(agentAccountRepository.findByEmail(accountRequest.getEmail()).isPresent()){
                    failed += 1;

                }else{
                    String password="Password@"+random.nextInt(9000)+1000;
                    AgentAccount agentAccount = new AgentAccount();
                    agentAccount.setEmail(accountRequest.getEmail());
                    agentAccount.setFullName(accountRequest.getFullName());
                    agentAccount.setContactNumber(accountRequest.getContactNumber());
                    agentAccount.setLastName(accountRequest.getLastName());
                    agentAccount.setPosition(accountRequest.getPosition());
                    agentAccount.setPassword(passwordEncoder.encode(password));
                    agentAccount.setCompanyId(UUID.fromString(companyId));
                    agentAccount.setExpired(false);
                    agentAccount.setStatus("Offline");
                    agentAccount.setStatusAgent("Active");
                    agentAccount.setOnLeave(false);
                    // Set the default profile picture for the user
                    Blob defaultProfilePicture = companyService.loadDefaultProfilePictureBlob();
                    agentAccount.setImage(defaultProfilePicture);
                    agentAccountRepository.save(agentAccount);
                    successful += 1;
                    agents+=1;
                    String content = this.setContent(accountRequest.getFullName(), accountRequest.getEmail(), password);
                    boolean isSent = sendEmailService.sendEmail(accountRequest.getEmail(), "Account Created",content);
                    if(isSent)
                        System.out.println("Email Sent");
                }
            }
        }

        return AccountResponse.builder()
                .failed(failed)
                .users(users)
                .agents(agents)
                .failedAccounts(failedAccount)
                .successful(successful).build();
    }

    public List<AgentResponse> getListOfAgents(UUID companyId){
        List<AgentAccount> agentAccounts = agentAccountRepository.findByCompanyId(companyId);
        return agentAccounts.stream().map(this::mapToAgent).toList();
    }
    private AgentResponse mapToAgent(AgentAccount agentAccount){
        return AgentResponse.builder()
                .accountId(agentAccount.getAccountId())
                .fullName(agentAccount.getFullName() +" "+agentAccount.getLastName())
                .isOnLeave(agentAccount.isOnLeave())
                .email(agentAccount.getEmail())
                .position(agentAccount.getPosition())
                .status(agentAccount.getStatus())
                .statusAgent(agentAccount.getStatusAgent())
                .build();
    }
    public List<UserResponse> getListOfUsers(UUID companyId){
        List<UserAccount> userAccounts = userAccountRepository.findByCompanyId(companyId);
        return userAccounts.stream().map(this::mapToUser).toList();

    }
    private UserResponse mapToUser(UserAccount userAccount){
        return UserResponse.builder()
                .accountId(userAccount.getAccountId())
                .fullName(userAccount.getFullName() +" "+userAccount.getLastName())
                .email(userAccount.getEmail())
                .position(userAccount.getPosition())
                .status(userAccount.getStatus())
                .statusAgent(userAccount.getStatusAgent())
                .contactNumber(userAccount.getContactNumber())
                .build();
    }
    public UserResponse getUser(UUID userAccountId){
        if (userAccountRepository.findById(userAccountId).isPresent()){
            UserAccount userAccount =userAccountRepository.findById(userAccountId).get();

            return UserResponse.builder()
                    .accountId(userAccount.getAccountId())
                    .fullName(userAccount.getFullName())
                    .position(userAccount.getPosition())
                    .email(userAccount.getEmail())
                    .lastName(userAccount.getLastName())
                    .contactNumber(userAccount.getContactNumber())
                    .build();
        } else if (agentAccountRepository.findById(userAccountId).isPresent()) {
            AgentAccount agentAccount = agentAccountRepository.findById(userAccountId).get();

            return UserResponse.builder()
                    .fullName(agentAccount.getFullName())
                    .email(agentAccount.getEmail())
                    .build();
        }
        return null;
    }

    public AgentResponse getAgent(UUID agentAccountId){

        if(agentAccountRepository.findById(agentAccountId).isPresent()){
            AgentAccount agentAccount= agentAccountRepository.findById(agentAccountId).get();
            return AgentResponse.builder()
                    .accountId(agentAccount.getAccountId())
                    .fullName(agentAccount.getFullName())
                    .isOnLeave(agentAccount.isOnLeave())
                    .email(agentAccount.getEmail())
                    .status(agentAccount.getStatus())
                    .statusAgent(agentAccount.getStatusAgent())
                    .contactNumber(agentAccount.getContactNumber())
                    .lastName(agentAccount.getLastName())
                    .position(agentAccount.getPosition())
                    .build();
        }
        return null;
    }

    public List<UserResponse> getAllUsers(String companyId){

        List<UserResponse> allUsersResponses = new ArrayList<>();

        List<AgentAccount> agents=agentAccountRepository.findByCompanyId(UUID.fromString(companyId));
        List<UserAccount> users = userAccountRepository.findByCompanyId(UUID.fromString(companyId));

        for(AgentAccount agentAccount:agents){
            UserResponse agent = UserResponse.builder()
                    .role("Agent")
                    .accountId(agentAccount.getAccountId())
                    .position(agentAccount.getPosition())
                    .fullName(agentAccount.getFullName()+" "+agentAccount.getLastName())
                    .email(agentAccount.getEmail()).build();
            allUsersResponses.add(agent);
        }

        for(UserAccount userAccount:users){
            UserResponse user = UserResponse.builder()
                    .role("End-User")
                    .accountId(userAccount.getAccountId())
                    .position(userAccount.getPosition())
                    .fullName(userAccount.getFullName()+" "+userAccount.getLastName())
                    .email(userAccount.getEmail())
                    .build();
            allUsersResponses.add(user);
        }
        return allUsersResponses;
    }

    public boolean updatePassword(String accountId,String role){
        boolean isSuccessful=false;
        LocalDate currentDate = LocalDate.now();

        String password = "Password@"+random.nextInt(9000)+1000;

        switch (role) {
            case "Agent" -> {
                if (agentAccountRepository.findById(UUID.fromString(accountId)).isPresent()) {
                    AgentAccount agentAccount = agentAccountRepository.findById(UUID.fromString(accountId)).get();
                    agentAccount.setPassword(passwordEncoder.encode(password));
                    agentAccountRepository.save(agentAccount);
                    isSuccessful = true;
                    String content = setContentForUpdatePassword(agentAccount.getFullName(), agentAccount.getEmail(), password);
                    boolean isSent = sendEmailService.sendEmail(agentAccount.getEmail(), "Password Changed",content);
                    if(isSent)
                        System.out.println("Email Sent");
                }
            }
            case "End-User" -> {
                if (userAccountRepository.findById(UUID.fromString(accountId)).isPresent()) {
                    UserAccount userAccount = userAccountRepository.findById(UUID.fromString(accountId)).get();
                    userAccount.setPassword(passwordEncoder.encode("Password@" + currentDate.getYear()));
                    userAccountRepository.save(userAccount);
                    isSuccessful = true;
                    String content = setContentForUpdatePassword(userAccount.getFullName(), userAccount.getEmail(), password);
                    boolean isSent = sendEmailService.sendEmail(userAccount.getEmail(), "Password Changed",content);
                    if(isSent)
                        System.out.println("Email Sent");
                }
            }
        }
       return isSuccessful;
    }


    public boolean changePassword(ChangePassword changePassword){
        boolean isPasswordChanged=false;
        switch (changePassword.getRole()) {
            case "[ROLE_AGENT]" -> {
                if (agentAccountRepository.findById(UUID.fromString(changePassword.getAccountId())).isPresent()) {
                    AgentAccount account = agentAccountRepository.findById(UUID.fromString(changePassword.getAccountId())).get();
                    account.setPassword(passwordEncoder.encode(changePassword.getPassword()));
                    agentAccountRepository.save(account);
                    isPasswordChanged = true;
                }
            }
            case "[ROLE_END_USER]" -> {
                if (userAccountRepository.findById(UUID.fromString(changePassword.getAccountId())).isPresent()) {
                    UserAccount userAccount = userAccountRepository.findById(UUID.fromString(changePassword.getAccountId())).get();
                    userAccount.setPassword(passwordEncoder.encode(changePassword.getPassword()));
                    userAccountRepository.save(userAccount);
                    isPasswordChanged = true;
                }
            }
        }
        return isPasswordChanged;
    }

    private String setContent(String fullName,String email,String password){
        String loginUrl = "https://localhost:4200/login";

        return  "Your Account has been created\n\n" +
                "Dear " + fullName + ",\n\n" +
                "Your account of service desk has been successfully created.\n\n" +
                "Username: " + email + "\n" +
                "Password: "+password+"\n\n" +
                "You can also change your password.\n\n" +
                "Thanks,\n" +
                "The Service Desk team";
    }

    private String setContentForUpdatePassword(String fullName,String email,String password){
        String loginUrl = "https://localhost:4200/login";

        return  "Your Password has been changed\n\n" +
                "Dear " + fullName + ",\n\n" +
                "Your password has been successfully changed.\n\n" +
                "Username: " + email + "\n" +
                "Password: "+password+"\n\n" +
                "You can also change your password.\n\n" +
                "Thanks,\n" +
                "The Service Desk team";
    }

    public UserData userData(UUID companyId){
        List<UserAccount> userAccount = userAccountRepository.findByCompanyId(companyId);
        List<AgentAccount> agentAccounts = agentAccountRepository.findByCompanyId(companyId);
        List<Administrator> administrators =administratorRepository.findByCompanyId(companyId);

        return UserData.builder()
                .admin(administrators.size())
                .endUsers(userAccount.size())
                .agents(agentAccounts.size()).build();
    }
    public void updateAgentStatus(String email, String statusAgent) {
        Optional<AgentAccount> optionalAgentAccount = agentAccountRepository.findByEmail(email);
        if (optionalAgentAccount.isPresent()) {
            AgentAccount agentAccount = optionalAgentAccount.get();
            agentAccount.setStatusAgent(statusAgent);
            agentAccountRepository.save(agentAccount);
        } else {
            // Handle case where agent with the given email is not found
            throw new EntityNotFoundException("Agent with email " + email + " not found.");
        }
    }
public void updateUserStatus(String email,String statusAgent){
    Optional<UserAccount> optionalUserAccount = userAccountRepository.findByEmail(email);
        if (optionalUserAccount.isPresent()) {
        UserAccount userAccount = optionalUserAccount.get();
        userAccount.setStatusAgent(statusAgent);
        userAccountRepository.save(userAccount);
    }else {
        // Handle case where agent with the given email is not found
        throw new EntityNotFoundException("Agent with email " + email + " not found.");
    }
}


    public ResponseEntity<Map<String, String>> updateProfile(String fullName, String lastName, String email,String position, Blob profileImageBlob) {
        Optional<UserAccount> userAccountOptional = userAccountRepository.findByEmail(email);
        Optional<AgentAccount> agentAccountOptional = agentAccountRepository.findByEmail(email);

        if (userAccountOptional.isPresent()) {
            UserAccount userAccount = userAccountOptional.get();
            userAccount.setFullName(fullName);
            userAccount.setLastName(lastName);
            userAccount.setEmail(email);
            userAccount.setPosition(position);

            if (profileImageBlob != null) {
                userAccount.setImage(profileImageBlob);
            }

            try {
                String token = companyService.generateToken(email);
                userAccountRepository.save(userAccount);
                Map<String, String> response = new HashMap<>();
                response.put("token", token);
                response.put("message", "Successfully updated");
                return ResponseEntity.ok(response);
            } catch (Exception e) {
                throw new RuntimeException("Failed to update user profile", e);
            }
        } else if (agentAccountOptional.isPresent()) {
            AgentAccount agentAccount = agentAccountOptional.get();
            agentAccount.setFullName(fullName);
            agentAccount.setLastName(lastName);
            agentAccount.setEmail(email);
            agentAccount.setPosition(position);
            if (profileImageBlob != null) {
                agentAccount.setImage(profileImageBlob);
            }

            try {
                String token = companyService.generateToken(email);
                agentAccountRepository.save(agentAccount);
                Map<String, String> response = new HashMap<>();
                response.put("token", token);
                response.put("message", "Successfully updated");
                return ResponseEntity.ok(response);
            } catch (Exception e) {
                throw new RuntimeException("Failed to update agent profile", e);
            }
        } else {
            throw new IllegalArgumentException("User or Agent with email " + email + " not found");
        }
    }

}