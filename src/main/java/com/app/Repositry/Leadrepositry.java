package com.app.Repositry;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.Entity.Company;
import com.app.Entity.Lead;
import com.app.Entity.USER;
import com.app.Enum.LeadStatus;
import com.app.Enum.Leadtype;

public interface Leadrepositry extends JpaRepository<Lead, Long>{
    List<Lead> findByAssignedTo(USER user);
    
    public List<Lead> findByNameContainingIgnoreCase(String name);
    
    public long countByLeadstatus(LeadStatus status);
    public long countByLeadtype(Leadtype type);

	boolean existsByEmail(String email);

    List<Lead> findByCompany(Company company);

	List<Lead> findByCompanyAndNameContainingIgnoreCase(Company company, String name);

	Long countByCompany(Company company);

	Long countByCompanyAndLeadstatus(Company company, LeadStatus new1);

	Long countByCompanyAndLeadtype(Company company, Leadtype hot);

	List<Lead> findByAssignedToAndCompany(USER user, Company company);

	boolean existsByEmailAndCompany(String email, Company company);
    

    

}
