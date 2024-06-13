package com.seohauniv.service;

import com.seohauniv.entity.Message;
import com.seohauniv.repository.MessageRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MessageService {
    private final MessageRepository messageRepository;

    public Message findById(Long messageId) {
        return messageRepository.findById(messageId).orElseThrow(EntityNotFoundException::new);
    }

    public Message create(Message message) {
        return messageRepository.save(message);
    }

    public Page<Message> findBySendToId(String memberId, Pageable pageable, String searchValue) {
        return messageRepository.myMessageList(memberId, pageable, searchValue);
    }

    public Message changeMessageStatus(Message message) {
        message.setIsRead("t");
        return message;
    }
}
