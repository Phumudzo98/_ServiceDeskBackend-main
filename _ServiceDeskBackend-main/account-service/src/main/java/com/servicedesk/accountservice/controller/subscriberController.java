package com.servicedesk.accountservice.controller;

import com.servicedesk.accountservice.dao.*;
import com.servicedesk.accountservice.service.SubscriberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RequestMapping("/api/subscriber")
@RestController
public class subscriberController {

    private final SubscriberService subscriberService;

    private final AuthenticationManager authenticationManager;

    private final SubscriberService service;



    @PostMapping(value = "/login",consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) throws Exception{
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken
                    (loginRequest.getEmail(),loginRequest.getPassword()));

        }catch (BadCredentialsException e){
            return ResponseEntity.badRequest().body(e);
        }

        return ResponseEntity.ok(new Response(service.generateToken(loginRequest.getEmail())));
    }

    @PostMapping(value = "/signup",consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> signup(@RequestBody SignupRequest signupRequest){

        if(subscriberService.signup(signupRequest))
            return ResponseEntity.ok(new Response("Successfully Registered Account"));

        return  ResponseEntity.badRequest().body("Error! Account already exist");
    }

    @GetMapping(value = "/get-company/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public CompanyResponse getCompanyInformation(@PathVariable("id") String id){
        return subscriberService.getCompany(UUID.fromString(id));
    }
    @PostMapping(value = "/registerAdministrator/{companyId}",consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> registerAdmin(@PathVariable("companyId") String companyId, @RequestBody AdminRequest adminRequest){
        adminRequest.setPassword("Password@2030");
        if(subscriberService.registerAdministrator(adminRequest,companyId)){
            return ResponseEntity.ok(new Response("Successfully Registered "+adminRequest.getFullName()
                    +" as Administrator"));
        }
        return  ResponseEntity.badRequest().body("Error! Administrator already exist");
    }

    @GetMapping(value = "/get-registered-admins/{companyId}",consumes = MediaType.APPLICATION_JSON_VALUE)
    public List<AdministratorResponse> getCompanyRegisteredAdmins(@PathVariable("companyId") String companyId){
        return service.getCompanyAdmins(UUID.fromString(companyId));
    }

    @GetMapping(value = "/get-admin/{email}",produces = MediaType.APPLICATION_JSON_VALUE)
    public AdminResponse getAdministrator(@PathVariable("email") String email){
        return service.getAdministrator(email);
    }

    @PutMapping(value = "/update-password/{role}")
    public ResponseEntity<?> changePassword(@PathVariable("role") String role,@RequestBody UpdatePasswordRequest updatePasswordRequest){
        if(service.changePassword(role,updatePasswordRequest))
            return ResponseEntity.ok(new Response("Password change successful"));
        return ResponseEntity.badRequest().body("Error! Failed to change password");
    }
}