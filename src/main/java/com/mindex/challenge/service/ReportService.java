package com.mindex.challenge.service;

import com.mindex.challenge.data.ReportingStructure;

public interface ReportService {
    //Get the tree for a user by id
    ReportingStructure read(String id);
}
