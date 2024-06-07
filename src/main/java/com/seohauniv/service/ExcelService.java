package com.seohauniv.service;

import com.seohauniv.dto.MemberFormDto;
import com.seohauniv.entity.Dept;
import com.seohauniv.entity.Professor;
import com.seohauniv.entity.Staff;
import com.seohauniv.entity.Student;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ExcelService {
    private final DeptService deptService;
    private final Validator memberFormDtoValidator;

    public Map<String, Object> readExcelFile(MultipartFile file) throws IOException {
        List<Staff> staffs = new ArrayList<>();
        List<Student> students = new ArrayList<>();
        List<Professor> professors = new ArrayList<>();
        List<String> errors = new ArrayList<>();

        Map<String, Object> result = new HashMap<>();

        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);
            for (int i = 1; i <= sheet.getLastRowNum(); i++) { // 첫 번째 행은 헤더이므로 건너뜀
                MemberFormDto memberFormDto = new MemberFormDto();

                Row row = sheet.getRow(i);
                if (row != null) {
                    if (row.getCell(1) == null) break;
                    if (row.getCell(2) == null) break;
                    if (row.getCell(3) == null) break;
                    if (row.getCell(4) == null) break;
                    if (row.getCell(5) == null) break;
                    memberFormDto.setRole(row.getCell(0).getStringCellValue());
                    memberFormDto.setName(row.getCell(1).getStringCellValue());
                    memberFormDto.setEmail(row.getCell(2).getStringCellValue());
                    memberFormDto.setBirth(row.getCell(3).getLocalDateTimeCellValue().toLocalDate());
                    memberFormDto.setPhone(row.getCell(4).getStringCellValue());
                    memberFormDto.setAddress(row.getCell(5).getStringCellValue());

                    if (row.getCell(6) != null) {
                        Dept dept = deptService.findByTitle(row.getCell(6).getStringCellValue());
                        memberFormDto.setDept(dept);
                    } else memberFormDto.setDept(null);

                    // 유효성 검사 수행
                    Errors validationErrors = new BeanPropertyBindingResult(memberFormDto, "memberFormDto");
                    memberFormDtoValidator.validate(memberFormDto, validationErrors);

                    if (validationErrors.hasErrors()) {
                        StringBuilder errorMessage = new StringBuilder((i + 1) + "번째 행: ");
                        validationErrors.getAllErrors().forEach(error -> errorMessage.append(error.getDefaultMessage()).append("\n"));
                        errors.add(errorMessage.toString());
                    } else {
                        switch (row.getCell(0).getStringCellValue()) {
                            case "STAFF":
                                Staff staff = new Staff(memberFormDto);
                                staffs.add(staff);
                                break;
                            case "STUDENT":
                                Student student = new Student(memberFormDto);
                                students.add(student);
                                break;
                            case "PROFESSOR":
                                Professor professor = new Professor(memberFormDto);
                                professors.add(professor);
                                break;
                        }
                    }
                }
            }
            result.put("STAFF", staffs);
            result.put("STUDENT", students);
            result.put("PROFESSOR", professors);
            result.put("errors", errors);
        }

        return result;
    }
}
