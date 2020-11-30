package com.example.handson;

public class JobInterestRecyclerViewItem {
    private String question;
    private boolean checkButton1;
    private boolean checkButton2;
    private boolean checkButton3;
    private boolean checkButton4;

    public JobInterestRecyclerViewItem(String question){
        this.question = question;
        checkButton1 = false;
        checkButton2 = false;
        checkButton3 = false;
        checkButton4 = false;
    }

    // getter
    public String getQuestion() {
        return question;
    }

    public boolean isCheckButton1() {
        return checkButton1;
    }

    public boolean isCheckButton2() {
        return checkButton2;
    }

    public boolean isCheckButton3() {
        return checkButton3;
    }

    public boolean isCheckButton4() {
        return checkButton4;
    }

    public int getCheckButtonNumber() {
        if (isCheckButton1()) {
            return 1;
        } else if (isCheckButton2()) {
            return 2;
        } else if (isCheckButton3()) {
            return 3;
        } else if (isCheckButton4()) {
            return 4;
        } else {
            return 0;
        }
    }

    public boolean isCheckButton(){
        if(checkButton1==true||checkButton2==true||checkButton3==true||checkButton4==true){
            return true;
        }
        return false;
    }

    // setter
    public void setQuestion(String question) {
        this.question = question;
    }

    public void setCheckButton1(boolean checkButton1) {
        this.checkButton1 = checkButton1;
    }

    public void setCheckButton2(boolean checkButton2) {
        this.checkButton2 = checkButton2;
    }

    public void setCheckButton3(boolean checkButton3) {
        this.checkButton3 = checkButton3;
    }

    public void setCheckButton4(boolean checkButton4) {
        this.checkButton4 = checkButton4;
    }

    public void setButtonClear(){
        checkButton1 = false;
        checkButton2 = false;
        checkButton3 = false;
        checkButton4 = false;
    }
}
