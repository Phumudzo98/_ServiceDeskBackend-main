package com.servicedesk.packageservice.controller;


import com.servicedesk.packageservice.dto.PackageDTO;
import com.servicedesk.packageservice.service.PackagesService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/packages")
public class PackagesController {
    private final PackagesService packagesService;

    @Value("${gateway.ip}")
    private String ipAddress;

    @PostMapping(value = "/post")
    @ResponseStatus(HttpStatus.CREATED)
    public  boolean storePackage(@RequestBody PackageDTO packageDAO){
        return packagesService.storePackage(packageDAO);
    }
    @PutMapping(value = "/update")
    @ResponseStatus(HttpStatus.OK)
    public boolean updatePackage(@RequestBody PackageDTO PackageDTO){
        return packagesService.updatePackage(PackageDTO);
    }
    @GetMapping(value = "/get/all")
    @ResponseStatus(HttpStatus.OK)
    public List<PackageDTO> getAllPackages(){
        return packagesService.getAllPackages();
    }
    @GetMapping(value = "/get/one/{id}")
    @ResponseStatus(HttpStatus.OK)
    public PackageDTO getOnePackage(@PathVariable("id") String id){
        return packagesService.getPackage(id);
    }
}
