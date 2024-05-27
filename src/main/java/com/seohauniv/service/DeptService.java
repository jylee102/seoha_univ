package com.seohauniv.service;

import com.seohauniv.entity.Dept;
import com.seohauniv.repository.DeptRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class DeptService {
    private final DeptRepository deptRepository;

    public Dept createDept(String deptTitle) {
        Dept dept = new Dept();
        dept.setTitle(deptTitle);

        return deptRepository.save(dept);
    }

    public Long count() {
        return deptRepository.count();
    }

    public List<Dept> getAllDept() {
        return deptRepository.findAll();
    }
}
