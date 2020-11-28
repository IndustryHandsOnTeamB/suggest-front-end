package com.example.handson;

import org.honorato.multistatetogglebutton.MultiStateToggleButton;

public class JobAptitudeRecyclerViewItem {

    private String question;
    MultiStateToggleButton mstb;

    public JobAptitudeRecyclerViewItem(String question){
        this.question = question;
    }

    public String getQuestion(){
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public int getMstbValue(){
        return mstb.getValue();
    }
}
