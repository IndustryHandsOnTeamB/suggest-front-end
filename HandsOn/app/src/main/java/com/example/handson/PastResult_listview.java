package com.example.handson;

public class PastResult_listview {
    private String date;
    private String testType;
    private String url;

    public PastResult_listview(String date, String testType, String url) {
        this.date = date;
        this.testType = testType;
        this.url=url;
    }

    //getter
    public String getDate() {
        return date;
    }

    public String getTestType() {
        return testType;
    }

    public String getUrl() {
        return url;
    }

    //setter
    public void setDate(String date) {
        this.date = date;
    }

    public void setTestType(String testType) {
        this.testType = testType;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
