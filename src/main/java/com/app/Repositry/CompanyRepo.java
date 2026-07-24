package com.app.Repositry;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.Dto.Companydto;
import com.app.Entity.Company;

public interface CompanyRepo extends JpaRepository<Company, Long>{


}
