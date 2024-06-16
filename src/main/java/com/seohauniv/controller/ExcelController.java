package com.seohauniv.controller;

import com.seohauniv.service.ExcelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class ExcelController {
    private final ExcelService excelService;
    private final SimpMessagingTemplate messagingTemplate;

    // 엑셀을 통한 구성원 등록
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

    // 엑셀을 통한 강의계획서 등록
    @PostMapping("/professor/upload/syllabus")
    public @ResponseBody ResponseEntity uploadSyllabusFile(@RequestParam("file") MultipartFile file, Principal principal) {

        try {
            String isNotValid = excelService.readSyllabusFile(file, messagingTemplate, principal.getName());

            if (isNotValid != null) { // 유효성 검사를 넘어가지 못함
                return new ResponseEntity(isNotValid, HttpStatus.BAD_REQUEST);
            } else {
                return new ResponseEntity("강의계획서 등록에 성공했습니다.", HttpStatus.OK);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity("파일 접근에 실패했습니다.", HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity("강의계획서 등록에 실패했습니다.", HttpStatus.BAD_REQUEST);
        }
    }
}