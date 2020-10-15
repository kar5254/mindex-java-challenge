package com.mindex.challenge;

import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.CompensationService;
import com.mindex.challenge.service.EmployeeService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ChallengeApplicationTests {

	private String reportStructureUrl;
	private String compensationGetUrl;
	private String compensationPostUrl;

	@Autowired
	private EmployeeService employeeService;

	@Autowired
	private CompensationService compensationService;

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;

	@Before
	public void setup() {
		reportStructureUrl = "http://localhost:" + port + "/report/{id}";
		compensationPostUrl = "http://localhost:" + port + "/compensation";
		compensationGetUrl = "http://localhost:" + port + "/compensation/{id}";
	}

	@Test
	public void testReportingStructure() {
		//Test an employee with levels of reports
		ReportingStructure highLevelEmployee = restTemplate.getForEntity(reportStructureUrl, ReportingStructure.class, "16a596ae-edd3-4847-99fe-c4518e82c86f").getBody();
		assertNotNull(highLevelEmployee);
		assertEquals(4, highLevelEmployee.getNumberOfReports());
		assertEquals("16a596ae-edd3-4847-99fe-c4518e82c86f", highLevelEmployee.getEmployee().getEmployeeId());

		//Test an employee with a single level of reports
		ReportingStructure midLevelEmployee = restTemplate.getForEntity(reportStructureUrl, ReportingStructure.class, "03aa1462-ffa9-4978-901b-7c001562cf6f").getBody();
		assertNotNull(midLevelEmployee);
		assertEquals(2, midLevelEmployee.getNumberOfReports());
		assertEquals("03aa1462-ffa9-4978-901b-7c001562cf6f", midLevelEmployee.getEmployee().getEmployeeId());

		//Test an employee with no reports
		ReportingStructure lowLevelEmployee = restTemplate.getForEntity(reportStructureUrl, ReportingStructure.class, "b7839309-3348-463b-a7e3-5de1c168beb3").getBody();
		assertNotNull(lowLevelEmployee);
		assertEquals(0, lowLevelEmployee.getNumberOfReports());
		assertEquals("b7839309-3348-463b-a7e3-5de1c168beb3", lowLevelEmployee.getEmployee().getEmployeeId());
	}

	@Test
	public void testCompensation() {
		//Test users
		Employee testManager = employeeService.read("16a596ae-edd3-4847-99fe-c4518e82c86f");
		Employee testDev = employeeService.read("b7839309-3348-463b-a7e3-5de1c168beb3");

		//Standard use case
		//Set up parameters
		Compensation managerBumpInput = new Compensation(testManager, 75000, "10/16/2020");
		//Post
		Compensation managerBump = restTemplate.postForEntity(compensationPostUrl, managerBumpInput, Compensation.class).getBody();
		assertNotNull(managerBump);
		assertEquals(75000, managerBump.getSalary());

		//Add multiple compensation reports to a user
		Compensation devComp1 = new Compensation(testDev, 65000, "10/16/2020");
		Compensation devComp2 = new Compensation(testDev, 70000, "10/16/2021");
		Compensation devBump1 = restTemplate.postForEntity(compensationPostUrl, devComp1, Compensation.class).getBody();
		Compensation devBump2 = restTemplate.postForEntity(compensationPostUrl, devComp2, Compensation.class).getBody();

		//Compare the current salary from a GET to the salary from the second pay bump, testing both. The salary should be updated with the latest post.
		Compensation finalSalary = restTemplate.getForEntity(compensationGetUrl, Compensation.class, "b7839309-3348-463b-a7e3-5de1c168beb3").getBody();
		assertNotNull(finalSalary);
		assertNotNull(devBump2);
		assertEquals(devBump2.getSalary(), finalSalary.getSalary());
		assertEquals(70000, finalSalary.getSalary());
	}
}
