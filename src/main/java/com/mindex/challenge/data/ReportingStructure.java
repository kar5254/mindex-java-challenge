package com.mindex.challenge.data;


/**
 * Output from the Task 1 endpoint. Represents org chart data relative to an employee.
 * The associated REST endpoint is accessible at localhost:8080/report
 */
public class ReportingStructure {
    //Instance Variables
    private Employee employee;
    private int numberOfReports;

    //Empty constructor, just in case
    public ReportingStructure () {
    }

    //Main constructor with variables
    public ReportingStructure (Employee employee, int numberOfReports) {
        this.employee = employee;
        this.numberOfReports = numberOfReports;
    }

    public Employee getEmployee() { return employee; }

    public void setEmployee(Employee employee) { this.employee = employee; }

    public int getNumberOfReports() { return numberOfReports; }

    public void setNumberOfReports(int numberOfReports) { this.numberOfReports = numberOfReports; }
}
