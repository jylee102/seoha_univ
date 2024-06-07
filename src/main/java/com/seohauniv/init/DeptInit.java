package com.seohauniv.init;

import com.seohauniv.service.DeptService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DeptInit implements ApplicationRunner {

    private final DeptService deptService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // 멤버가 없는 경우에만 초기화 작업 실행
        if (deptService.count() == 0) {
            String[] deptNames = {"컴퓨터공학학과", "의예과", "경영학과", "법학과", "경제학과", "신문방송학과", "기계공학과", "국어국문학과", "영어영문학과"};

            for (String deptName : deptNames) {
                deptService.createDept(deptName);
            }
        }
    }
}