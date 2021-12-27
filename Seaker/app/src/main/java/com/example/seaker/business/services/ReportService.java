package com.example.seaker.business.services;

public class ReportService {
    private static ReportService instance=null;

    public ReportService() {
    }

    public static ReportService getInstance(){
        if(instance==null) instance=new ReportService();
        return instance;
    }
}
