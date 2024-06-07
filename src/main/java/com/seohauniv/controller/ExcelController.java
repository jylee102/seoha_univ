package com.seohauniv.controller;

import com.seohauniv.dto.MemberFormDto;
import com.seohauniv.entity.Dept;
import com.seohauniv.entity.Professor;
import com.seohauniv.entity.Staff;
import com.seohauniv.entity.Student;
import com.seohauniv.service.DeptService;
import com.seohauniv.service.ExcelService;
import com.seohauniv.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class ExcelController {
    private final ExcelService excelService;
    private final DeptService deptService;
    private final MemberService memberService;

    @PostMapping("/staff/upload/members")
    public String uploadFile(@RequestParam("file") MultipartFile file, Model model) {

        List<Dept> deptList = deptService.getAllDept();

        try {
            Map<String, Object> list = excelService.readExcelFile(file);

            List<String> errors = (List<String>) list.get("errors");
            if (!errors.isEmpty()) {
                model.addAttribute("message", list.get("errors"));

                model.addAttribute("memberFormDto", new MemberFormDto());
                model.addAttribute("deptList", deptList);
                return "staff/memberForm";
            }

            List<Student> students = (List<Student>) list.get("STUDENT");
            for (Student student : students) {
                memberService.createMember(student);
            }
            List<Professor> professors = (List<Professor>) list.get("PROFESSOR");
            for (Professor professor : professors) {
                memberService.createMember(professor);
            }
            List<Staff> staffs = (List<Staff>) list.get("STAFF");
            for (Staff staff : staffs) {
                memberService.createMember(staff);
            }

            model.addAttribute("message", "구성원 등록에 성공했습니다.");

            model.addAttribute("memberFormDto", new MemberFormDto());
            model.addAttribute("deptList", deptList);
            return "staff/memberForm";
        } catch (IOException e) {
            e.printStackTrace();
            model.addAttribute("message", "파일 접근에 실패했습니다.");

            model.addAttribute("memberFormDto", new MemberFormDto());
            model.addAttribute("deptList", deptList);
            return "staff/memberForm";
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("message", "구성원 등록에 실패했습니다.");

            model.addAttribute("memberFormDto", new MemberFormDto());
            model.addAttribute("deptList", deptList);
            return "staff/memberForm";
        }
    }
}