package com.mindex.challenge;

import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
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

	@Autowired
	private EmployeeService employeeService;

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;

	@Before
	public void setup() {
		reportStructureUrl = "http://localhost:" + port + "/report/{id}";
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

}
