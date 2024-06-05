package com.servicedesk.accountservice.service;

import com.servicedesk.accountservice.dao.*;
import com.servicedesk.accountservice.model.Account;
import com.servicedesk.accountservice.model.Company;
import com.servicedesk.accountservice.repository.AccountRepository;
import com.servicedesk.accountservice.repository.AdministratorRepository;
import com.servicedesk.accountservice.repository.CompanyRepository;
import com.servicedesk.accountservice.model.*;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.sql.Date;
import java.time.Instant;
import java.util.Base64;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SubscriberService {

    @Value("${token.secret}")
    private String tokenSecret;

    @Value("${token.expiration_time}")
    private String expirationTime;

    private final UserDetailsService userDetailsService;

    private final CompanyRepository companyRepository;
    private final AdministratorRepository administratorRepository;
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

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
        return true;
    }
    public CompanyResponse getCompany(UUID companyId){
        Company company = companyRepository.findById(companyId).get();

        CompanyResponse companyResponse = CompanyResponse.builder()
                .companyId(company.getCompanyId())
                .companyName(company.getCompanyName())
                .companyEmail(company.getCompanyEmail())
                .contactNumber(company.getContactNumber())
                .build();
        return companyResponse;
    }
    public boolean registerAdministrator(AdminRequest adminRequest,String companyId){

        if(companyRepository.findById(UUID.fromString(companyId)).isPresent() &&
                administratorRepository.findByEmail(adminRequest.getEmail()).isEmpty()){
            Company company = companyRepository.findById(UUID.fromString(companyId)).get();
            Administrator administrator = Administrator.builder()
                    .fullName(adminRequest.getFullName())
                    .email(adminRequest.getEmail())
                    .contactNumber(adminRequest.getContactNumber())
                    .lastName(adminRequest.getLastName())
                    .position(adminRequest.getPosition())
                    .password(passwordEncoder.encode(adminRequest.getPassword()))
                    .company(company).build();
            administratorRepository.save(administrator);
            return true;
        }
        return false;
    }

    public String generateToken(String email){

        UserDto userDto = getUserDetails(email);

        UserDetails userDetails = userDetailsService.loadUserByUsername(email);


        byte[] secretKeyBytes = Base64.getEncoder().encode(tokenSecret.getBytes());
        SecretKey secretKey = new SecretKeySpec(secretKeyBytes, SignatureAlgorithm.HS512.getJcaName());
        Instant now = Instant.now();

        return Jwts.builder()
                .setSubject(userDto.getCompanyName())
                .claim("companyId",userDto.getCompanyId())
                .claim("userEmail",userDto.getEmail())
                .claim("name",userDto.getFullName()==null? "Null": userDto.getFullName())
                .claim("role",userDetails.getAuthorities().toString())
                .setExpiration(Date.from(now.plusMillis(Long.parseLong(expirationTime))))
                .setIssuedAt(Date.from(now))
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();
    }

    private UserDto getUserDetails(String email){
        if( accountRepository.findByUsername(email).isPresent()){
            Account account = accountRepository.findByUsername(email).get();
            if(account==null) throw  new UsernameNotFoundException("User not found");
            return UserDto.builder()
                    .companyId(account.getCompany().getCompanyId().toString())
                    .email(account.getCompany().getCompanyEmail())
                    .companyName(account.getCompany().getCompanyName())
                    .build();
        }else{
            Administrator administrator = administratorRepository.findByEmail(email).get();
            if(administrator==null) throw  new UsernameNotFoundException("User not found");
            return UserDto.builder()
                    .companyId(administrator.getCompany().getCompanyId().toString())
                    .fullName(administrator.getFullName()+" "+administrator.getLastName())
                    .email(administrator.getEmail())
                    .companyName(administrator.getCompany().getCompanyName())
                    .build();
        }
    }

    public AdminResponse getAdministrator(String email){
        if(administratorRepository.findByEmail(email).isPresent()){
            Administrator administrator = administratorRepository.findByEmail(email).get();
            return AdminResponse.builder()
                    .email(administrator.getEmail())
                    .password(administrator.getPassword())
                    .build();
        }
        return null;
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

        if (role.equalsIgnoreCase("ROLE_SUBSCRIBER")) {
            Account account = accountRepository.findByUsername(passwordRequest.getEmail())
                    .orElseThrow(() -> new NoSuchElementException("Account not found"));
            System.out.println(account);
            isSuccessful = true;

        } else if (role.equalsIgnoreCase("ROLE_ADMIN")) {
            Administrator administrator = administratorRepository.findByEmail(passwordRequest.getEmail())
                    .orElseThrow(() -> new NoSuchElementException("Administrator not found"));
            System.out.println(administrator);
            isSuccessful = true;
        }

        return isSuccessful;
    }
}