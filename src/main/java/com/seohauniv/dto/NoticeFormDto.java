package com.seohauniv.dto;

import com.seohauniv.entity.Notice;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;

@Getter
@Setter
public class NoticeFormDto {

    private Long id;

    @NotBlank(message = "제목은 필수 입력입니다.")
    private String title;

    @NotBlank(message = "내용은 필수 입력입니다.")
    private String content;

    private LocalDateTime regTime;

    private int views;

    private static ModelMapper modelMapper = new ModelMapper();

    public Notice creatNotice() {
        return modelMapper.map(this, Notice.class);
    }

    public static NoticeFormDto of(Notice notice) {
        return modelMapper.map(notice, NoticeFormDto.class);
    }
}
