package com.servicedesk.usersservice.service;

import com.servicedesk.usersservice.dao.ResetPassword;
import com.servicedesk.usersservice.dao.UserResponse;
import com.servicedesk.usersservice.dao.company.SignupRequest;
import com.servicedesk.usersservice.model.AgentAccount;
import com.servicedesk.usersservice.model.UserAccount;
import com.servicedesk.usersservice.model.company.Account;
import com.servicedesk.usersservice.model.company.Administrator;
import com.servicedesk.usersservice.model.company.Company;
import com.servicedesk.usersservice.repository.AgentAccountRepository;
import com.servicedesk.usersservice.repository.UserAccountRepository;
import com.servicedesk.usersservice.repository.company.AdministratorRepository;
import com.servicedesk.usersservice.repository.company.CompanyRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.SQLException;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;
import org.apache.commons.io.IOUtils;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthService {

    private final CompanyRepository companyRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserDetailsService userDetailsService;
    private final AgentAccountRepository agentAccountRepository;
    private final AdministratorRepository administratorRepository;
    private final UserAccountRepository userAccountRepository;
    private final CompanyService companyService;
    @Autowired
    JavaMailSender javaMailSender;
    @Value("${token.secret}")
    private String tokenSecret;

    @Value("${token.expiration_time}")
    private String expirationTime;

    public boolean signup(SignupRequest signupRequest){
        if(companyRepository.findByCompanyEmail(signupRequest.getCompanyEmail()).isPresent()){
            return  false;
        }

        Company company =Company.builder()
                .companyEmail(signupRequest.getCompanyEmail())
                .contactNumber(signupRequest.getContactNumber())
                .companyName(signupRequest.getCompanyName()).build();

        Account account = Account.builder()
                .password(passwordEncoder.encode(signupRequest.getPassword()))
                .username(signupRequest.getCompanyEmail()).build();

        company.setAccount(account);
        account.setCompany(company);

        companyRepository.save(company);

        Optional<Company> companyOptional = companyRepository.findByCompanyEmail(signupRequest.getCompanyEmail());

        UUID companyId= companyOptional.map(Company::getCompanyId).orElse(null);

        companyService.registerAdministrator(signupRequest, String.valueOf(companyId));

        return true;
    }
    public void loginUser(UserResponse userResponse) {
        String email = userResponse.getEmail();
        Optional<AgentAccount> optionalAgentAccount = agentAccountRepository.findByEmail(email);
        if (optionalAgentAccount.isPresent()) {
            AgentAccount agentAccount = optionalAgentAccount.get();
            agentAccount.setStatus("Online");
            agentAccountRepository.save(agentAccount);
        }
    }
    public void logoutUser(String email) {
        Optional<AgentAccount> optionalAgentAccount = agentAccountRepository.findByEmail(email);
        if (optionalAgentAccount.isPresent()) {
            AgentAccount agentAccount = optionalAgentAccount.get();
            agentAccount.setStatus("Offline");
            agentAccountRepository.save(agentAccount);
        }
    }
    public AgentAccount getAgentByEmail(String email) {
        return agentAccountRepository.findByEmail(email).orElse(null);
    }
    public  UserAccount getUserByEmail(String email){
        return  userAccountRepository.findByEmail(email).orElse(null);
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
    public String resetPassword(ResetPassword resetPassword) {
        String email = resetPassword.getEmail();
        String newPassword = resetPassword.getPassword();

        Optional<UserAccount> optionalUserAccount = userAccountRepository.findByEmail(email);
        Optional<AgentAccount> optionalAgentAccount = agentAccountRepository.findByEmail(email);
        Optional<Administrator> optionalAdministrator = administratorRepository.findByEmail(email);

        // Check if the email belongs to a UserAccount
        if (optionalUserAccount.isPresent()) {
            UserAccount userAccount = optionalUserAccount.get();
            String hashedPassword = passwordEncoder.encode(newPassword);
            userAccount.setPassword(hashedPassword);
            userAccountRepository.save(userAccount);
            return "{\"message\": \"Password changed successfully for UserAccount\"}";
        }

        // Check if the email belongs to an AgentAccount
        if (optionalAgentAccount.isPresent()) {
            AgentAccount agentAccount = optionalAgentAccount.get();
            String hashedPassword = passwordEncoder.encode(newPassword);
            agentAccount.setPassword(hashedPassword);
            agentAccountRepository.save(agentAccount);
            return "{\"message\": \"Password changed successfully for AgentAccount\"}";
        }

        // Check if the email belongs to an Administrator
        if (optionalAdministrator.isPresent()) {
            Administrator administrator = optionalAdministrator.get();
            String hashedPassword = passwordEncoder.encode(newPassword);
            administrator.setPassword(hashedPassword);
            administratorRepository.save(administrator);
            return "{\"message\": \"Password changed successfully for Administrator\"}";
        }

        // If no account found for the email
        return "{\"message\": \"User not found\"}";
    }


    public String sendEmail(Object account) {
        try {
            SimpleMailMessage msg = new SimpleMailMessage();
            msg.setFrom("nhlanhlakhoza05@gmail.com");

            if (account instanceof Administrator) {
                Administrator admin = (Administrator) account;
                msg.setTo(admin.getEmail());
                msg.setSubject("Password Reset Request");
                msg.setText("Hello " + admin.getFullName() + ",\n\n"
                        + "You have requested to reset your password.\n\n"
                        + "Please click on the following link to reset your password: " + "http://localhost:4200/change-password?"+admin.getEmail() + "\n\n"
                        + "If you did not request this, please ignore this email.\n\n"
                        + "Regards,\n"
                        + "Service Desk System");
            } else if (account instanceof AgentAccount) {
                AgentAccount agent = (AgentAccount) account;
                msg.setTo(agent.getEmail());
                msg.setSubject("Password Reset Request");
                msg.setText("Hello " + agent.getFullName() + ",\n\n"
                        + "You have requested to reset your password.\n\n"
                        + "Please click on the following link to reset your password: " + "http://localhost:4200/agent-change-password?"+agent.getEmail() + "\n\n"
                        + "If you did not request this, please ignore this email.\n\n"
                        + "Regards,\n"
                        + "Service Desk System");
            } else if (account instanceof UserAccount) {
                UserAccount user = (UserAccount) account;
                msg.setTo(user.getEmail());
                msg.setSubject("Password Reset Request");
                msg.setText("Hello " + user.getFullName() + ",\n\n"
                        + "You have requested to reset your password.\n\n"
                        + "Please click on the following link to reset your password: " + "http://localhost:4200/employee-change-password?"+user.getEmail() + "\n\n"
                        + "If you did not request this, please ignore this email.\n\n"
                        + "Regards,\n"
                        + "Service Desk System");
            } else {
                return "error";
            }

            javaMailSender.send(msg);
            System.out.println("Mail Send...");
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }


}
