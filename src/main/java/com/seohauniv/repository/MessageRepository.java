package com.seohauniv.repository;

import com.seohauniv.entity.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MessageRepository extends JpaRepository<Message, Long> {

    @Query("SELECT m FROM Message m " +
            "JOIN m.sendTo member " +
            "WHERE member.id = :id " +
            "AND (LOWER(m.title) LIKE LOWER(CONCAT('%', :searchValue, '%')) " +
            "OR LOWER(m.content) LIKE LOWER(CONCAT('%', :searchValue, '%'))) " +
            "ORDER BY m.regDate DESC")
    Page<Message> myMessageList(@Param("id") String id, Pageable pageable, @Param("searchValue") String searchValue);
}
