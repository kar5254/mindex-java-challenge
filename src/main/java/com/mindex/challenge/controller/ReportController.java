package com.mindex.challenge.controller;

import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.ReportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ReportController {
    private static final Logger LOG = LoggerFactory.getLogger(ReportController.class);

    @Autowired
    private ReportService reportService; //The interface of this microservice

    @GetMapping("/report/{id}")
    public ReportingStructure read(@PathVariable String id) {
        LOG.debug("Received report structure request for id [{}]", id);

        return reportService.read(id);
    }
}
