package com.example.handson;

import org.honorato.multistatetogglebutton.MultiStateToggleButton;

public class JobAptitudeRecyclerViewItem {

    private String question;
    private int mstbVal;

    public JobAptitudeRecyclerViewItem(String question){
        this.question = question;
        this.mstbVal = -1; //초기화
    }

    public boolean isCheckMstb(){
        if(mstbVal != -1){
            return true;
        }
        return false;
    }

    public int getMstbVal() {
        return mstbVal;
    }

    public void setMstbVal(int position) {
        this.mstbVal = position;
    }

    public String getQuestion(){
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

}
