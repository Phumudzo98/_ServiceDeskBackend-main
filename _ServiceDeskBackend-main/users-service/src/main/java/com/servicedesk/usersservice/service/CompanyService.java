package com.servicedesk.usersservice.service;

import com.servicedesk.usersservice.dao.company.*;
import com.servicedesk.usersservice.model.AgentAccount;
import com.servicedesk.usersservice.model.UserAccount;
import com.servicedesk.usersservice.model.company.Account;
import com.servicedesk.usersservice.model.company.Administrator;
import com.servicedesk.usersservice.model.company.Company;
import com.servicedesk.usersservice.repository.AgentAccountRepository;
import com.servicedesk.usersservice.repository.UserAccountRepository;
import com.servicedesk.usersservice.repository.company.AccountRepository;
import com.servicedesk.usersservice.repository.company.AdministratorRepository;
import com.servicedesk.usersservice.repository.company.CompanyRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.SQLException;
import java.time.Instant;
import java.util.*;

@Service
@RequiredArgsConstructor
public class CompanyService {

    private final CompanyRepository companyRepository;
    private final AdministratorRepository administratorRepository;
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final SendEmailService sendEmailService;
    private final UserDetailsService userDetailsService;
    private final AgentAccountRepository agentAccountRepository;
    private final UserAccountRepository userAccountRepository;
    private  AuthService authService;
    @Value("${token.secret}")
    private String tokenSecret;

    @Value("${token.expiration_time}")
    private String expirationTime;

    public CompanyResponse getCompany(UUID companyId){
        Optional<Company> companyOptional = companyRepository.findById(companyId);
        if(companyOptional.isPresent()) {
            Company company = companyOptional.get();

            return CompanyResponse.builder()
                    .companyId(company.getCompanyId())
                    .companyName(company.getCompanyName())
                    .companyEmail(company.getCompanyEmail())
                    .contactNumber(company.getContactNumber())
                    .build();
        }

        return null;
    }

    public boolean registerAdministrator(SignupRequest signupRequest, String companyId){

        if(companyRepository.findById(UUID.fromString(companyId)).isPresent() &&
                administratorRepository.findByEmail(signupRequest.getEmail()).isEmpty()){
            Company company = companyRepository.findById(UUID.fromString(companyId)).get();

            Administrator administrator = Administrator.builder()
                    .fullName(signupRequest.getFullName())
                    .email(signupRequest.getEmail())
                    .contactNumber(signupRequest.getContactNumber())
                    .lastName(signupRequest.getLastName())
                    .position(signupRequest.getPosition())
                    .password(passwordEncoder.encode(signupRequest.getAdminPassword()))
                    .company(company).build();

            // Set the default profile picture for the user
            Blob defaultProfilePicture = loadDefaultProfilePictureBlob();
            administrator.setImage(defaultProfilePicture);
            administrator.setProfileChange("On");
            administrator.setPasswordChange("On");
            administratorRepository.save(administrator);
            String content = setContent(administrator.getFullName(), administrator.getEmail(), signupRequest.getPassword());
            boolean isSent = sendEmailService.sendEmail(administrator.getEmail(), "Admin Account Created",content);
            if(isSent)
                System.out.println("Email sent");
            return true;
        }
        return false;
    }
    public void updatePassChangeNoti(String email, String passwordChange){
        Optional<Administrator> optionalAdministrator=administratorRepository.findByEmail(email);

        if(optionalAdministrator.isPresent()){
            Administrator administrator=optionalAdministrator.get();
            administrator.setPasswordChange(passwordChange);
            administratorRepository.save(administrator);
        }else {
            // Handle case where Administrator with the given email is not found
            throw new EntityNotFoundException("Administrator with email " + email + " not found.");
        }
    }
    public void updateProfileChangeNoti(String email, String profileChange){
        Optional<Administrator> optionalAdministrator=administratorRepository.findByEmail(email);

        if(optionalAdministrator.isPresent()){
            Administrator administrator=optionalAdministrator.get();
            administrator.setProfileChange(profileChange);
            administratorRepository.save(administrator);
        }else {
            // Handle case where Administrator with the given email is not found
            throw new EntityNotFoundException("Administrator with email " + email + " not found.");
        }
    }
    public List<AdministratorResponse> getCompanyAdmins(UUID companyId){
        List<Administrator> administrators = administratorRepository.findByCompanyId(companyId);
        return  administrators.stream().map(this::mapToAdministrator).toList();
    }

    private AdministratorResponse mapToAdministrator(Administrator administrator){
        return AdministratorResponse.builder()
                .email(administrator.getEmail())
                .fullName(administrator.getFullName())
                .position(administrator.getPosition())
                .build();
    }


    public boolean changePassword(String role, UpdatePasswordRequest passwordRequest){
        boolean isSuccessful=false;

        if (role.equalsIgnoreCase("ROLE_COMPANY")) {
            if(accountRepository.findByUsername(passwordRequest.getEmail()).isPresent()){
                Account account = accountRepository.findByUsername(passwordRequest.getEmail()).get();
                account.setPassword(passwordRequest.getPassword());
                accountRepository.save(account);
                isSuccessful=true;
            }
        } else if (role.equalsIgnoreCase("ROLE_ADMIN")) {
            if(administratorRepository.findByEmail(passwordRequest.getEmail()).isPresent()) {
                Administrator administrator = administratorRepository.findByEmail(passwordRequest.getEmail()).get();
                administrator.setPassword(passwordRequest.getPassword());
                administratorRepository.save(administrator);
                isSuccessful = true;
            }
        }
        return isSuccessful;
    }

    private String setContent(String fullName,String email,String password){
        String loginUrl = "https://localhost:4200/login";

        return  "Your Account has been created\n\n" +
                "Dear " + fullName + ",\n\n" +
                "Your account as Administrator of service desk has been successfully created.\n\n" +
                "Username: " + email + "\n" +
                "Password: "+password+"\n\n" +
                "You can also change your password.\n\n" +
                "Click <a href=\"" + loginUrl + "\">here</a> to login.\n\n" +
                "Thanks,\n" +
                "The Service Desk team";
    }

    public Blob loadDefaultProfilePictureBlob() {
        try {
            // Load the default profile picture from a predefined location or resource
            InputStream inputStream = getClass().getResourceAsStream("/images/default2.jpg");
            if (inputStream != null) {
                byte[] bytes = IOUtils.toByteArray(inputStream);
                return new SerialBlob(bytes); // Convert byte array to Blob
            }
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
        return null; // Return null if default picture is not found or cannot be loaded
    }

    public ResponseEntity<Map<String, String>> updateProfile(String fullName, String lastName, String email,String position, Blob profileImageBlob) {
        // Retrieve the Administrator by email
        Administrator administrator = administratorRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User with email " + email + " not found"));

        // Update user's profile information
        administrator.setLastName(lastName);
        administrator.setFullName(fullName);
        administrator.setEmail(email);
        administrator.setPosition(position);

        // Update profile image only if provided
        if (profileImageBlob != null) {
            administrator.setImage(profileImageBlob);
        }

        try {
           String token=generateToken(email);
            administratorRepository.save(administrator);
            // Return success message as a JSON object
            Map<String, String> response = new HashMap<>();
            response.put("token", token);
            response.put("message", "Successfully updated");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            // Handle exceptions appropriately
            throw new RuntimeException("Failed to update user profile", e);
        }
    }

    public String generateToken(String email){

        UUID companyId=null;
        UUID accountID=null;
        String fullName=null;
        String companyName=null;
        String lastName=null;
        String position=null;

        if(agentAccountRepository.findByEmail(email).isPresent()) {
            AgentAccount agentAccount = agentAccountRepository.findByEmail(email).get();
            if("Active".equalsIgnoreCase(agentAccount.getStatusAgent())) {
                companyId = agentAccount.getCompanyId();
                accountID = agentAccount.getAccountId();
                fullName = agentAccount.getFullName() + " " + agentAccount.getLastName();
                lastName = agentAccount.getLastName();
                fullName=agentAccount.getFullName();
                position=agentAccount.getPosition();
                // companyName=
            }
        }else if(userAccountRepository.findByEmail(email).isPresent()){
            UserAccount userAccount = userAccountRepository.findByEmail(email).get();
            companyId=userAccount.getCompanyId();
            accountID=userAccount.getAccountId();
            fullName= userAccount.getFullName()+" "+userAccount.getLastName();
            lastName=userAccount.getLastName();
            fullName=userAccount.getFullName();
            position=userAccount.getPosition();
            //companyName= Objects.requireNonNull(companyRepository.findById(userAccount.getCompanyId()).orElse(null)).getCompanyName();
        }else if(companyRepository.findByCompanyEmail(email).isPresent()){
            Company company = companyRepository.findByCompanyEmail(email).get();
            companyId=company.getCompanyId();
            companyName= company.getCompanyName();
        }else if(administratorRepository.findByEmail(email).isPresent()){
            Administrator administrator = administratorRepository.findByEmail(email).get();
            companyId=administrator.getCompany().getCompanyId();
            accountID=administrator.getAdminId();
            fullName= administrator.getFullName()+" "+administrator.getLastName();
            companyName= administrator.getCompany().getCompanyName();
            lastName=administrator.getLastName();
            fullName=administrator.getFullName();
            position=administrator.getPosition();
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(email);

        byte[] secretKeyBytes = Base64.getEncoder().encode(tokenSecret.getBytes());
        SecretKey secretKey = new SecretKeySpec(secretKeyBytes, SignatureAlgorithm.HS512.getJcaName());
        Instant now = Instant.now();

        return Jwts.builder()
                .setSubject(companyName)
                .claim("companyId",companyId)
                .claim("accountId",accountID==null?companyId:accountID)
                .claim("name",fullName==null? "Null": fullName)
                .claim("role",userDetails.getAuthorities().toString())
                .claim("email",email)
                .claim("lastName",lastName)
                .claim("firstName",fullName)
                .claim("position",position)
                .setExpiration(Date.from(now.plusMillis(Long.parseLong(expirationTime))))
                .setIssuedAt(Date.from(now))
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();
    }


}
