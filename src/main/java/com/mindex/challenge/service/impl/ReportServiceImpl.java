package com.mindex.challenge.service.impl;

import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.ReportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class ReportServiceImpl implements ReportService {

    private static final Logger LOG = LoggerFactory.getLogger(ReportServiceImpl.class);

    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public ReportingStructure read(String id) {
        LOG.debug("Creating structure for employee with ID [{}]", id);

        //Initialize instance variables for our new structure
        int reports;
        Employee employee = employeeRepository.findByEmployeeId(id);

        if (employee == null) {
            throw new RuntimeException("Invalid employeeId: " + id);
        }

        //Get the number of reports by iterating over the employee tree
        reports = numReportsHelper(employee);

        return new ReportingStructure(employee, reports);
    }

    /**
     * A recursive helper function to iterate over the employee database and count direct reports.
     * @param employee The employee on which to start our traversal
     * @return The number of direct and indirect reports of the employee
     */
    private int numReportsHelper (Employee employee) {
        //If we have no direct reports, return 0 for this node
        if (Objects.isNull(employee.getDirectReports())) {
            return 0;
        } else {
            //Initialize this node's reports with its direct reports
            int numReports = employee.getDirectReports().size();
            for (Employee e : employee.getDirectReports()) {
                //Recurse through direct reports to find indirect reports
                numReports += numReportsHelper(employeeRepository.findByEmployeeId(e.getEmployeeId()));
            }
            return numReports;
        }
    }
}
