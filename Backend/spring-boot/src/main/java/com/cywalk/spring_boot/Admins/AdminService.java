package com.cywalk.spring_boot.Admins;

import com.cywalk.spring_boot.Organizations.Organization;
import com.cywalk.spring_boot.Organizations.OrganizationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AdminService {

    @Autowired
    AdminRepository adminRepository;

    @Autowired
    AdminCredentialRepository adminCredentialRepository;

    @Autowired
    AdminSessionRepository adminSessionRepository;
    @Autowired
    private OrganizationRepository organizationRepository;

    public boolean adminExistsForOrganization(String name, Organization organization) {
        return adminRepository.findByNameAndOrganization(name, organization).isEmpty();
    }

    public Optional<Admin> getAdminByCombinedName(String combinedName) {
        return adminRepository.findById(combinedName);
    }

    AdminCredentials fromAdminModel(AdminModel adminModel, Organization organization) {
        Admin newAdmin = new Admin(organization, adminModel.getUsername());
        return new AdminCredentials(newAdmin, adminModel.getPassword());
    }

    public AdminSession signUpAdmin(AdminModel adminModel, Organization organization) {
        AdminCredentials adminCredentials = fromAdminModel(adminModel, organization);
        adminCredentials.setAdmin(adminRepository.save(adminCredentials.getAdmin()));
        organization.addAdmin(adminCredentials.getAdmin());
        adminCredentials = adminCredentialRepository.save(adminCredentials);
        adminRepository.save(adminCredentials.getAdmin());
        organization = organizationRepository.save(organization);

        AdminSession adminSession = new AdminSession(adminCredentials.getAdmin(), organization.getId());
        return adminSessionRepository.save(adminSession);
    }

    public boolean adminExists(AdminModel adminModel) {
        return adminCredentialRepository.findById(adminModel.getUsername()).isEmpty();
    }

    public Optional<AdminSession> loginAdmin(AdminModel adminModel) {
        Optional<AdminCredentials> adminCredentialsResult = adminCredentialRepository.findById(adminModel.getUsername());
        if (adminCredentialsResult.isEmpty()) {
            return Optional.empty();
        }
        AdminCredentials adminCredentials = adminCredentialsResult.get();
        if (adminCredentials.getPassword().equals(adminModel.getPassword())) {
            AdminSession adminSession = new AdminSession(adminCredentials.getAdmin(), adminCredentials.getAdmin().getOrganization().getId());
            return Optional.of(adminSessionRepository.save(adminSession));
        }
        return Optional.empty();
    }

    public Optional<Admin> getAdminFromSession(Long key) {
        Optional<AdminSession> adminSession = adminSessionRepository.findById(key);
        return adminSession.map(AdminSession::getAdmin);
    }
}