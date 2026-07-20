package com.app.Repositry;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

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
    

    

}
