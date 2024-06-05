package com.servicedesk.packageservice.service;

import com.servicedesk.packageservice.dto.PackageDTO;
import com.servicedesk.packageservice.model.Packages;
import com.servicedesk.packageservice.repository.PackagesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PackagesService {
    private final PackagesRepository packagesRepository;
    public boolean storePackage(PackageDTO packageRequest){
        if(packagesRepository.findByPackageName(packageRequest.getPackageName()).isPresent()){
            return false;
        }
        Packages packages = Packages.builder()
                .packageName(packageRequest.getPackageName())
                .description(packageRequest.getDescription())
                .price(packageRequest.getPrice())
                .period(packageRequest.getPeriod())
                .userLimit(packageRequest.getUserLimit())
                .build();
        packagesRepository.save(packages);
        return true;
    }
    public boolean updatePackage(PackageDTO packageDAO){
        if(packagesRepository.existsById(packageDAO.getPackageId())){
            Packages packages = Packages.builder()
                    .packageName(packageDAO.getPackageName())
                    .description(packageDAO.getDescription())
                    .price(packageDAO.getPrice())
                    .period(packageDAO.getPeriod())
                    .userLimit(packageDAO.getUserLimit())
                    .build();
            packagesRepository.save(packages);
            return true;
        }
        return false;
    }
    public List<PackageDTO> getAllPackages(){
        List<Packages> packages = packagesRepository.findAll();
        return packages.stream().map(this::mapToProductDAO).toList();
    }
    public PackageDTO getPackage(String id){
        Packages packages = packagesRepository.findById(id).get();
        return PackageDTO.builder()
                .packageId(packages.getPackageId())
                .packageName(packages.getPackageName())
                .description(packages.getPackageName())
                .price(packages.getPrice())
                .period(packages.getPeriod())
                .userLimit(packages.getUserLimit())
                .build();
    }
    private PackageDTO mapToProductDAO(Packages packages){
        return PackageDTO.builder()
                .packageId(packages.getPackageId())
                .packageName(packages.getPackageName())
                .description(packages.getDescription())
                .price(packages.getPrice())
                .userLimit(packages.getUserLimit())
                .period(packages.getPeriod()).build();
    }
}