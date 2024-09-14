package com.jabguru.companyms.company;

import com.jabguru.companyms.company.dto.ReviewMessage;

import java.util.List;

public interface CompanyService {
    List<Company> getAllCompanies();
    void createCompany(Company company);
    Company getCompanyById(Long id);
    boolean deleteCompanyById(Long id);
    boolean updateCompany(Long id, Company updatedCompany);
    public void updateCompanyRating(ReviewMessage reviewMessage);
}
