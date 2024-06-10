package com.seohauniv.controller;

import com.seohauniv.dto.MemberFormDto;
import com.seohauniv.dto.ProgressUpdate;
import com.seohauniv.entity.Dept;
import com.seohauniv.entity.Professor;
import com.seohauniv.entity.Staff;
import com.seohauniv.entity.Student;
import com.seohauniv.service.DeptService;
import com.seohauniv.service.ExcelService;
import com.seohauniv.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class ExcelController {
    private final ExcelService excelService;

    private final SimpMessagingTemplate messagingTemplate;

    @PostMapping("/staff/upload/members")
    public @ResponseBody ResponseEntity uploadFile(@RequestParam("file") MultipartFile file) {

        try {
            String isNotValid = excelService.readExcelFile(file, messagingTemplate);

            if (isNotValid != null) { // 유효성 검사를 넘어가지 못함
                return new ResponseEntity(isNotValid, HttpStatus.BAD_REQUEST);
            } else {
                return new ResponseEntity("구성원 등록에 성공했습니다.", HttpStatus.OK);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity("파일 접근에 실패했습니다.", HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity("구성원 등록에 실패했습니다.", HttpStatus.BAD_REQUEST);
        }
    }
}