package com.seohauniv.controller;

import com.seohauniv.dto.SyllabusFormDto;
import com.seohauniv.entity.Message;
import com.seohauniv.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class MessageController {
    private final MessageService messageService;

    @GetMapping("/loadMessageList")
    public @ResponseBody ResponseEntity loadMessageList(@RequestParam(value = "page", defaultValue = "0") Optional<Integer> page,
                                                        @RequestParam(value = "searchValue", defaultValue = "") String searchValue, Principal principal) {
        try {
            Pageable pageable = PageRequest.of(page.orElse(0), 5);
            Page<Message> messagePage = messageService.findBySendToId(principal.getName(), pageable, searchValue);
            return new ResponseEntity(messagePage, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity("메시지 목록을 불러오는 것에 실패했습니다.\n관리자에게 문의하세요.", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/changeMessageStatus/{messageId}")
    public @ResponseBody ResponseEntity changeMessageStatus(@PathVariable("messageId") Long messageId) {
        try {
            messageService.changeMessageStatus(messageId);
            return new ResponseEntity(messageId, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity("서버에 이상이 있습니다.\n관리자에게 문의하세요.", HttpStatus.BAD_REQUEST);
        }
    }
}
