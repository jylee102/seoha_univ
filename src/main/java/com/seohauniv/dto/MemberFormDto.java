package com.seohauniv.dto;

import com.seohauniv.entity.Dept;
import com.seohauniv.entity.Member;
import com.seohauniv.validation.DeptNotRequiredForStaff;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.modelmapper.ModelMapper;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
@DeptNotRequiredForStaff // 커스텀 어노테이션(dept가 필수입력값인지)
public class MemberFormDto {

    @NotBlank(message = "이름은 필수 입력 값입니다.")
    private String name;

    @NotEmpty(message = "이메일은 필수 입력 값입니다.")
    @Email(message = "이메일 형식으로 입력해주세요.")
    private String email;

    @Pattern(regexp="^$|^\\d{3}-\\d{3,4}-\\d{4}$", message="전화번호 형식이 올바르지 않습니다.")
    private String phone;

    private String address;

    @NotNull(message = "생일은 필수 입력 값입니다.")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birth;

    private Dept dept;

    private String role;

    private static ModelMapper modelMapper = new ModelMapper();

    // entity -> dto
    public static MemberFormDto of(Member member) {
        MemberFormDto memberFormDto = modelMapper.map(member, MemberFormDto.class);

        switch (member.getRole().toString()) {
            case "STAFF":
                memberFormDto.setPhone(member.getStaff().getPhone());
                memberFormDto.setAddress(member.getStaff().getAddress());
                memberFormDto.setEmail(member.getStaff().getEmail());
                memberFormDto.setBirth(member.getStaff().getBirth());
                break;
            case "STUDENT":
                memberFormDto.setPhone(member.getStudent().getPhone());
                memberFormDto.setAddress(member.getStudent().getAddress());
                memberFormDto.setEmail(member.getStudent().getEmail());
                memberFormDto.setBirth(member.getStudent().getBirth());
                memberFormDto.setDept(member.getStudent().getMajor());
                break;
            case "PROFESSOR":
                memberFormDto.setPhone(member.getProfessor().getPhone());
                memberFormDto.setAddress(member.getProfessor().getAddress());
                memberFormDto.setEmail(member.getProfessor().getEmail());
                memberFormDto.setBirth(member.getProfessor().getBirth());
                memberFormDto.setDept(member.getProfessor().getMajor());
                break;
        }

        return memberFormDto;
    }

    // dto -> entity
    public Member createMember() {
        return modelMapper.map(this, Member.class);
    }
}
