package com.seohauniv.util;

import java.time.LocalDateTime;
import java.util.Random;

public class IdGenerator {
    private static final Random RANDOM = new Random();

    public static String generateStudentId(LocalDateTime regDate) {
        String admissionYear = String.valueOf(regDate.getYear()).substring(2);
        int uniquePart = RANDOM.nextInt(100000); // 5자리 숫자 생성
        return String.format("%s%05d", admissionYear, uniquePart);
    }

    public static String generateStaffId(LocalDateTime regDate) {
        String admissionYear = String.valueOf(regDate.getYear()).substring(2);
        int uniquePart = RANDOM.nextInt(10000); // 4자리 숫자 생성
        return String.format("%s%04d", admissionYear, uniquePart);
    }

    public static String generateProfessorId(LocalDateTime regDate) {
        String admissionYear = String.valueOf(regDate.getYear()).substring(2);
        int uniquePart = RANDOM.nextInt(1000); // 3자리 숫자 생성
        return String.format("%s%03d", admissionYear, uniquePart);
    }
}
