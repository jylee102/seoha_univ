package com.seohauniv.controller;

import com.seohauniv.entity.Message;
import com.seohauniv.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class MessageController {
    private final MessageService messageService;
    private final UserDetailsService userDetailsService;

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

    // 메시지 상태 변경(안읽음->읽음)
    @PostMapping("/changeMessageStatus/{messageId}")
    public @ResponseBody ResponseEntity changeMessageStatus(@PathVariable("messageId") Long messageId, Principal principal) {
        try {
            Message message = messageService.findById(messageId);

            // 이미 읽은 상태의 메시지라면 메시지 상태 변경 불필요
            if (message.getIsRead().equals("t")) return new ResponseEntity(message, HttpStatus.OK);
            
            messageService.changeMessageStatus(message);

            // 인증 정보를 다시 로드하여 최신 상태로 업데이트
            UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);

            return new ResponseEntity(message, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity("서버에 이상이 있습니다.\n관리자에게 문의하세요.", HttpStatus.BAD_REQUEST);
        }
    }
}
