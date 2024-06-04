package com.seohauniv.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;

@EntityListeners(value = {AuditingEntityListener.class}) // audit 기능을 사용하기 위해 작성
@MappedSuperclass
@Getter
@Setter
public abstract class BaseTimeEntity {

    @CreatedDate // 최초 등록한 날짜를 저장 및 감지
    @Column(updatable = false) // 해당 컬럼에 대한 값은 업데이트 X
    private LocalDateTime regDate; //등록일


    @LastModifiedDate // 수정한 날짜를 저장 및 감지
    private LocalDateTime updateDate; //수정일
}