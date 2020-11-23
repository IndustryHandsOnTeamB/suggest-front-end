package com.example.handson;

public class PastResult_listview {
    private String date;
    private String testType;
    private String result;

    public PastResult_listview(String date, String testType, String result) {
        this.date = date;
        this.testType = testType;
        this.result = result;
    }

    //getter
    public String getDate() {
        return date;
    }

    public String getTestType() {
        return testType;
    }

    public String getResult() {
        return result;
    }

    //setter
    public void setDate(String date) {
        this.date = date;
    }

    public void setTestType(String testType) {
        this.testType = testType;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
