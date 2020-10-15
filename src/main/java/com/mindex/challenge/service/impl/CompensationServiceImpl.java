package com.mindex.challenge.service.impl;

import com.mindex.challenge.dao.CompensationRepository;
import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.service.CompensationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class CompensationServiceImpl implements CompensationService {
    private static final Logger LOG = LoggerFactory.getLogger(CompensationServiceImpl.class);

    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private CompensationRepository compensationRepository;


    @Override
    public Compensation create(Compensation comp) {
        LOG.debug("Creating compensation for [{}]", comp);
        if (Objects.isNull(compensationRepository.findByEmployee(comp.getEmployee()))) {
            compensationRepository.insert(comp);
        } else {
            LOG.debug("Compensation already exists, updating with [{}]", comp);
            compensationRepository.save(comp);
        }

        return comp;
    }

    @Override
    public Compensation read(String id) {
        LOG.debug("Retrieving compensation for id [{}]", id);
        Employee fromId = employeeRepository.findByEmployeeId(id);
        if (fromId == null) { throw new RuntimeException("Invalid employeeId: " + id); }
        Compensation comp = compensationRepository.findByEmployee(fromId);
        if (comp == null) { throw new RuntimeException("No compensation log for employee: " + id); }
        return comp;
    }
}
