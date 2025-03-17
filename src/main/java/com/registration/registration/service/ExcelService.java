package com.registration.registration.service;




import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.registration.registration.model.Person;
import com.registration.registration.repository.PersonRepository;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class ExcelService {

    private final PersonRepository personRepository;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Autowired
    public ExcelService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public ByteArrayInputStream exportToExcel() throws IOException {
        List<Person> people = personRepository.findAll();
        
        try (Workbook workbook = new XSSFWorkbook(); 
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            
            Sheet sheet = workbook.createSheet("People");
            
            // Create header row
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("ID");
            headerRow.createCell(1).setCellValue("Name");
            headerRow.createCell(2).setCellValue("NRC");
            headerRow.createCell(3).setCellValue("Date of Birth");
            headerRow.createCell(4).setCellValue("Father's Name");
            headerRow.createCell(5).setCellValue("Phone");
            headerRow.createCell(6).setCellValue("Email");
            headerRow.createCell(7).setCellValue("Township");
            headerRow.createCell(8).setCellValue("Address");
            
            // Create data rows
            int rowIdx = 1;
            for (Person person : people) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(person.getId());
                row.createCell(1).setCellValue(person.getName());
                row.createCell(2).setCellValue(person.getNrc());
                if (person.getDob() != null) {
                    row.createCell(3).setCellValue(person.getDob().toString());
                }
                row.createCell(4).setCellValue(person.getFatherName());
                row.createCell(5).setCellValue(person.getPhone());
                row.createCell(6).setCellValue(person.getEmail());
                row.createCell(7).setCellValue(person.getTownship());
                row.createCell(8).setCellValue(person.getAddress());
            }
            
            // Resize columns to fit content
            for (int i = 0; i < 9; i++) {
                sheet.autoSizeColumn(i);
            }
            
            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        }
    }
    
    public List<Person> importFromExcel(MultipartFile file) throws IOException {
        List<Person> importedPeople = new ArrayList<>();
        List<String> errors = new ArrayList<>();
        
        try (InputStream is = file.getInputStream(); 
             Workbook workbook = WorkbookFactory.create(is)) {
            
            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rows = sheet.iterator();
            
            // Skip header row
            if (rows.hasNext()) {
                rows.next();
            }
            
            int rowNumber = 1;
            while (rows.hasNext()) {
                Row currentRow = rows.next();
                rowNumber++;
                
                try {
                    // Skip empty rows
                    if (isEmptyRow(currentRow)) {
                        continue;
                    }
                    
                    Person person = new Person();
                    
                    // Extract cell values
                    person.setName(getCellValueAsString(currentRow.getCell(1)));
                    person.setNrc(getCellValueAsString(currentRow.getCell(2)));
                    
                    String dobString = getCellValueAsString(currentRow.getCell(3));
                    if (dobString != null && !dobString.isEmpty()) {
                        try {
                            person.setDob(LocalDate.parse(dobString, DATE_FORMATTER));
                        } catch (DateTimeParseException e) {
                            errors.add("Row " + rowNumber + ": Invalid date format. Use YYYY-MM-DD.");
                        }
                    }
                    
                    person.setFatherName(getCellValueAsString(currentRow.getCell(4)));
                    person.setPhone(getCellValueAsString(currentRow.getCell(5)));
                    person.setEmail(getCellValueAsString(currentRow.getCell(6)));
                    person.setTownship(getCellValueAsString(currentRow.getCell(7)));
                    person.setAddress(getCellValueAsString(currentRow.getCell(8)));
                    
                    // Validate required fields
                    if (person.getName() == null || person.getName().isEmpty()) {
                        errors.add("Row " + rowNumber + ": Name is required.");
                    }
                    
                    if (person.getNrc() == null || person.getNrc().isEmpty()) {
                        errors.add("Row " + rowNumber + ": NRC is required.");
                    }
                    
                    // Add to list if no validation errors
                    if (person.getName() != null && !person.getName().isEmpty() &&
                        person.getNrc() != null && !person.getNrc().isEmpty()) {
                        importedPeople.add(person);
                    }
                    
                } catch (Exception e) {
                    errors.add("Row " + rowNumber + ": " + e.getMessage());
                }
            }
            
            // If there are errors, throw exception with error messages
            if (!errors.isEmpty()) {
                throw new IOException("Import errors: " + String.join(", ", errors));
            }
            
            // Save all valid records
            return personRepository.saveAll(importedPeople);
        }
    }
    
    private boolean isEmptyRow(Row row) {
        if (row == null) {
            return true;
        }
        if (row.getCell(1) == null) {
            return true;
        }
        return getCellValueAsString(row.getCell(1)).isEmpty();
    }
    
    private String getCellValueAsString(Cell cell) {
        if (cell == null) {
            return "";
        }
        
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getLocalDateTimeCellValue().toLocalDate().toString();
                }
                return String.valueOf((long) cell.getNumericCellValue());
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            default:
                return "";
        }
    }
}