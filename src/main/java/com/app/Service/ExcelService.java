package com.app.Service;

import java.io.IOException;
import java.time.LocalDate;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.app.Entity.Lead;
import com.app.Entity.USER;
import com.app.Enum.LeadStatus;
import com.app.Enum.Leadtype;
import com.app.Enum.Status;
import com.app.Repositry.Leadrepositry;
import com.app.Repositry.Userreop;

@Service
public class ExcelService {

	@Autowired
	private Leadrepositry leadrepo;
	@Autowired
	private Userreop userrepo;

	public String uploadLeads(MultipartFile file) {

		int imported = 0;
		int skipped = 0;
		
		String useremail = SecurityContextHolder.getContext().getAuthentication().getName();

		USER admin = userrepo.findByEmail(useremail)
		        .orElseThrow(() -> new RuntimeException("Admin not found"));

		try (Workbook workbook = WorkbookFactory.create(file.getInputStream())) {

			Sheet sheet = workbook.getSheetAt(0);

			for (int i = 1; i <= sheet.getLastRowNum(); i++) {

				Row row = sheet.getRow(i);

				if (row == null) {
					continue;
				}

				String email = row.getCell(3).getStringCellValue();

				if (leadrepo.existsByEmailAndCompany(email, admin.getCompany())) {
				    skipped++;
				    continue;
				}

				Lead lead = new Lead();

				lead.setName(row.getCell(0).getStringCellValue());
				lead.setCity(row.getCell(1).getStringCellValue());

				lead.setDob(row.getCell(2).getLocalDateTimeCellValue().toLocalDate());

				lead.setEmail(email);
				lead.setCompany(admin.getCompany());

				lead.setArea(row.getCell(4).getStringCellValue());

				lead.setPhone(String.valueOf((long) row.getCell(5).getNumericCellValue()));

				lead.setCustomerBudget(row.getCell(6).getNumericCellValue());

				lead.setSocialMediaName(row.getCell(7).getStringCellValue());

				lead.setRemark(row.getCell(8).getStringCellValue());

				lead.setLeadtype(Leadtype.valueOf(row.getCell(9).getStringCellValue().trim().toUpperCase()));

				lead.setLeadstatus(LeadStatus.NEW);
				lead.setStaus(Status.ACTIVE);
				lead.setExecutedBy("Excel Import");

				leadrepo.save(lead);

				imported++;
			}

		} catch (IOException e) {
			throw new RuntimeException("Unable to read Excel file", e);
		}

		return "Import Completed Successfully.\n" + "Imported : " + imported + "\n" + "Skipped : " + skipped;
	}
}