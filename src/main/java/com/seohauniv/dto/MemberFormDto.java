package com.seohauniv.dto;

import com.seohauniv.entity.Dept;
import com.seohauniv.entity.Member;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.modelmapper.ModelMapper;

import java.time.LocalDate;

@Getter
@Setter
public class MemberFormDto {

    @NotBlank(message = "이름은 필수 입력 값입니다.")
    private String name;

    @NotEmpty(message = "이메일은 필수 입력 값입니다.")
    @Email(message = "이메일 형식으로 입력해주세요.")
    private String email;

    @Pattern(regexp="^\\d{3}-\\d{3,4}-\\d{4}$", message="전화번호 형식이 올바르지 않습니다.")
    private String phone;

    private String address;

    private LocalDate birth;

    private Dept dept;

    private String role;

    private static ModelMapper modelMapper = new ModelMapper();

    // entity -> dto
    public static MemberFormDto of(Member member) {
        return modelMapper.map(member, MemberFormDto.class);
    }

    // dto -> entity
    public Member createMember() {
        return modelMapper.map(this, Member.class);
    }
}
