package com.seohauniv.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@EntityListeners(value={AuditingEntityListener.class}) //audit기능을 사용하기 위해 작성
@MappedSuperclass //엔티티에서 부모클래스로 사용
@Getter
@Setter
public abstract class BaseEntity extends BaseTimeEntity {

    @CreatedBy
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "member_id", updatable = false)
    private Member createdBy;

}
