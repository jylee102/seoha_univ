package com.seohauniv.service;

import com.seohauniv.constant.CourseType;
import com.seohauniv.constant.Day;
import com.seohauniv.dto.CourseTimeDto;
import com.seohauniv.dto.MemberFormDto;
import com.seohauniv.dto.ProgressUpdate;
import com.seohauniv.dto.SyllabusFormDto;
import com.seohauniv.entity.*;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExcelService {
    private final DeptService deptService;
    private final MemberService memberService;
    private final Validator memberFormDtoValidator;

    private final ProfessorService professorService;
    private final SyllabusService syllabusService;
    private final Validator syllabusFormDtoValidator;

    // 구성원 파일 읽기
    public String readExcelFile(MultipartFile file, SimpMessagingTemplate messagingTemplate) throws IOException {
        List<Staff> staffs = new ArrayList<>();
        List<Student> students = new ArrayList<>();
        List<Professor> professors = new ArrayList<>();
        List<String> errors = new ArrayList<>();

        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);

            int dataRows = getDataRows(sheet);

            for (int i = 1; i <= dataRows; i++) { // 첫 번째 행은 헤더이므로 건너뜀
                Row row = sheet.getRow(i);
                if (row != null) {
                    // 각 행의 데이터를 통해 memberFormDto 만들기
                    MemberFormDto memberFormDto = getMemberFormDto(row);

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

                    // 파일 읽는 것에 대한 진행 상황
                    int percentComplete = (int) ((i / (double) dataRows) * 100);
                    String message = "파일 읽는 중: " + (i + 1) + " / " + (dataRows + 1);
                    messagingTemplate.convertAndSend("/topic/progress", new ProgressUpdate(percentComplete, message));
                }
            }
        }

        if (!errors.isEmpty()) {
            return errors.toString().replaceAll(",", "\n");
        } else {
            processDatabaseUpdates(staffs, students, professors, messagingTemplate);
            return null;
        }
    }

    // 구성원 등록
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

    // 강의 계획서 파일 읽기
    public String readSyllabusFile(MultipartFile file, SimpMessagingTemplate messagingTemplate, String professorId) throws IOException {
        Professor professor = professorService.findById(professorId);

        List<SyllabusFormDto> syllabusFormDtoList = new ArrayList<>();
        List<String> errors = new ArrayList<>();

        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);

            int dataRows = getDataRows(sheet);

            for (int i = 1; i <= dataRows; i++) { // 첫 번째 행은 헤더이므로 건너뜀
                Row row = sheet.getRow(i);
                if (row != null) {
                    // 각 행의 데이터를 통해 syllabusFormDto 만들기
                    SyllabusFormDto syllabusFormDto = getSyllabusFormDto(row, i);

                    // 유효성 검사 수행
                    Errors validationErrors = new BeanPropertyBindingResult(syllabusFormDto, "syllabusFormDto");
                    syllabusFormDtoValidator.validate(syllabusFormDto, validationErrors);

                    if (validationErrors.hasErrors()) {
                        StringBuilder errorMessage = new StringBuilder((i + 1) + "번째 행: ");
                        validationErrors.getAllErrors().forEach(error -> errorMessage.append(error.getDefaultMessage()));
                        errors.add(errorMessage.toString());
                    } else {
                        syllabusFormDtoList.add(syllabusFormDto);
                    }
                }

                // 파일 읽는 것에 대한 진행 상황
                int percentComplete = (int) ((i / (double) dataRows) * 100);
                String message = "파일 읽는 중: " + (i + 1) + " / " + (dataRows + 1);
                messagingTemplate.convertAndSend("/topic/progress", new ProgressUpdate(percentComplete, message));
            }
        }

        if (!errors.isEmpty()) {
            return errors.toString().replaceAll(",", "\n");
        } else {
            List<SyllabusFormDto> failedSyllabuses = processDatabaseUpdates(syllabusFormDtoList, professor, messagingTemplate);

            if (!failedSyllabuses.isEmpty()) {
                return "다음 항목들이 등록에 실패했습니다: " + failedSyllabuses.toString();
            } else {
                return null;
            }
        }
    }

    // 강의 계획서 등록
    private List<SyllabusFormDto> processDatabaseUpdates(List<SyllabusFormDto> syllabusFormDtoList, Professor professor, SimpMessagingTemplate messagingTemplate) {
        List<SyllabusFormDto> failedSyllabuses = new ArrayList<>();

        // 강의계획서 등록
        for (int i = 0; i < syllabusFormDtoList.size(); i++) {
            try {
                syllabusService.create(syllabusFormDtoList.get(i), professor);

                int percentComplete = (int) (((i + 1) / (double) syllabusFormDtoList.size()) * 100);
                String message = "강의계획서 등록 중: " + (i + 1) + " / " + syllabusFormDtoList.size();
                messagingTemplate.convertAndSend("/topic/progress", new ProgressUpdate(percentComplete, message));
            } catch (Exception e) {
                e.printStackTrace();
                failedSyllabuses.add(syllabusFormDtoList.get(i));
            }
        }

        return failedSyllabuses;
    }

    // 데이터가 있는 행의 개수를 세는 메소드
    private int getDataRows(Sheet sheet) {
        int dataRows = 0; // 데이터가 있는 행의 개수
        int lastRowNum = sheet.getLastRowNum();

        for (int i = 1; i <= lastRowNum; i++) {
            Row row = sheet.getRow(i);
            if (row != null && row.getCell(0) != null) {
                dataRows++;
            }
        }

        return dataRows;
    }

    // 엑셀의 데이터를 통해 memberFormDto 생성
    private MemberFormDto getMemberFormDto(Row row) {
        MemberFormDto memberFormDto = new MemberFormDto();

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

        return memberFormDto;
    }

    // 엑셀의 데이터를 통해 syllabusFormDto 생성
    private SyllabusFormDto getSyllabusFormDto(Row row, int i) {
        SyllabusFormDto syllabusFormDto = new SyllabusFormDto();

        syllabusFormDto.setYear((int) row.getCell(0).getNumericCellValue());
        syllabusFormDto.setSemester((int) row.getCell(1).getNumericCellValue());
        syllabusFormDto.setCourseName(row.getCell(2).getStringCellValue());
        syllabusFormDto.setCourseType(CourseType.valueOf(row.getCell(3).getStringCellValue()));
        syllabusFormDto.setCredit((int) row.getCell(4).getNumericCellValue());
        syllabusFormDto.setCapacity((int) row.getCell(5).getNumericCellValue());
        syllabusFormDto.setOverview(row.getCell(6).getStringCellValue());
        syllabusFormDto.setObjective(row.getCell(7).getStringCellValue());
        syllabusFormDto.setTextbook(row.getCell(8).getStringCellValue());

        List<CourseTimeDto> courseTimes = new ArrayList<>();
        for (int j = 9; j < 18; j += 3) {
            if (row.getCell(j) == null) break;

            CourseTimeDto courseTimeDto = new CourseTimeDto();

            switch (row.getCell(j).getStringCellValue()) {
                case "월요일":
                    courseTimeDto.setDay(Day.MON);
                    break;
                case "화요일":
                    courseTimeDto.setDay(Day.TUE);
                    break;
                case "수요일":
                    courseTimeDto.setDay(Day.WED);
                    break;
                case "목요일":
                    courseTimeDto.setDay(Day.THU);
                    break;
                case "금요일":
                    courseTimeDto.setDay(Day.FRI);
                    break;
            }

            if (row.getCell(j + 1).getCellType() == CellType.NUMERIC) {
                courseTimeDto.setStartTime(row.getCell(j + 1).getDateCellValue().toInstant().atZone(ZoneId.systemDefault()).toLocalTime());
            } else {
                throw new IllegalStateException(i + "행," + (j+1) + "열: 시작시간의 데이터타입이 적절하지 않습니다.");
            }

            if (row.getCell(j + 2).getCellType() == CellType.NUMERIC) {
                courseTimeDto.setEndTime(row.getCell(j + 2).getDateCellValue().toInstant().atZone(ZoneId.systemDefault()).toLocalTime());
            } else {
                throw new IllegalStateException(i + "행: 종료시간의 데이터타입이 적절하지 않습니다.");
            }
            courseTimes.add(courseTimeDto);
        }
        syllabusFormDto.setCourseTimes(courseTimes);

        List<WeeklyPlan> weeklyPlans = new ArrayList<>();
        for (int j = 1; j <= 16; j++) {
            WeeklyPlan weeklyPlan = new WeeklyPlan();
            weeklyPlan.setWeek(j);
            if (row.getCell(j + 18) == null) weeklyPlan.setContent("");
            else weeklyPlan.setContent(row.getCell(j + 18).getStringCellValue());
            weeklyPlans.add(weeklyPlan);
        }
        syllabusFormDto.setWeeklyPlans(weeklyPlans);

        return syllabusFormDto;
    }
}
