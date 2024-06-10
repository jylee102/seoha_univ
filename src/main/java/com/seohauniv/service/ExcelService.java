package com.seohauniv.service;

import com.seohauniv.dto.MemberFormDto;
import com.seohauniv.dto.ProgressUpdate;
import com.seohauniv.entity.Dept;
import com.seohauniv.entity.Professor;
import com.seohauniv.entity.Staff;
import com.seohauniv.entity.Student;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
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
    private final MemberService memberService;
    private final Validator memberFormDtoValidator;

    public String readExcelFile(MultipartFile file, SimpMessagingTemplate messagingTemplate) throws IOException {
        List<Staff> staffs = new ArrayList<>();
        List<Student> students = new ArrayList<>();
        List<Professor> professors = new ArrayList<>();
        List<String> errors = new ArrayList<>();

        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);

            int dataRows = 0; // 데이터가 있는 행의 개수

            // 데이터가 있는 행의 개수를 세는 반복문
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row != null && row.getCell(0) != null) {
                    dataRows++; // 데이터가 있는 행이므로 개수를 증가시킴
                }
            }

            for (int i = 1; i <= dataRows; i++) { // 첫 번째 행은 헤더이므로 건너뜀
                MemberFormDto memberFormDto = new MemberFormDto();

                Row row = sheet.getRow(i);
                if (row != null) {
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

                    // 이메일 중복 검사
                    if (!validationErrors.hasErrors()) {
                        if (memberService.existsByEmail(memberFormDto.getEmail())) {
                            validationErrors.rejectValue("email", "field.duplicate", "이미 등록된 이메일입니다.");
                        }
                    }

                    if (validationErrors.hasErrors()) {
                        StringBuilder errorMessage = new StringBuilder((i + 1) + "번째 행: ");
                        validationErrors.getAllErrors().forEach(error -> errorMessage.append(error.getDefaultMessage()));
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

                // 파일 읽는 것에 대한 진행 상황
                int percentComplete = (int) ((i / (double) dataRows) * 100);
                String message = "진행상황: " + (i + 1) + " / " + (dataRows + 1);
                messagingTemplate.convertAndSend("/topic/progress", new ProgressUpdate(percentComplete, message));
            }
        }

        if (!errors.isEmpty()) {
            return errors.toString().replaceAll(",", "\n");
        } else {
            processDatabaseUpdates(staffs, students, professors, messagingTemplate);
            return null;
        }
    }

    private void processDatabaseUpdates(List<Staff> staffs, List<Student> students, List<Professor> professors, SimpMessagingTemplate messagingTemplate) {
        // 학생 등록
        for (int i = 0; i < students.size(); i++) {
            memberService.createMember(students.get(i));

            int percentComplete = (int) (((i + 1) / (double) students.size()) * 100);
            String message = "학생 등록 중: " + (i + 1) + " / " + students.size();
            messagingTemplate.convertAndSend("/topic/progress", new ProgressUpdate(percentComplete, message));
        }

        // 교수 등록
        for (int i = 0; i < professors.size(); i++) {
            memberService.createMember(professors.get(i));

            int percentComplete = (int) (((i + 1) / (double) professors.size()) * 100);
            String message = "교수 등록 중: " + (i + 1) + " / " + professors.size();
            messagingTemplate.convertAndSend("/topic/progress", new ProgressUpdate(percentComplete, message));
        }

        // 교직원 등록
        for (int i = 0; i < staffs.size(); i++) {
            memberService.createMember(staffs.get(i));

            int percentComplete = (int) (((i + 1) / (double) staffs.size()) * 100);
            String message = "교직원 등록 중: " + (i + 1) + " / " + staffs.size();
            messagingTemplate.convertAndSend("/topic/progress", new ProgressUpdate(percentComplete, message));
        }
    }
}
