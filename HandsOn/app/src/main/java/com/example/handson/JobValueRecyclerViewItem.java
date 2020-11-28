package com.example.handson;

import org.honorato.multistatetogglebutton.MultiStateToggleButton;

public class JobValueRecyclerViewItem {

    //question에 가치관 2개를 넣기
    private String[] question;
    MultiStateToggleButton mstb;

    public MultiStateToggleButton getMstb() {
        return mstb;
    }

    public JobValueRecyclerViewItem(String[] question){
        this.question = question;
    }

    public String getQuestion_text1() {
        return question[0];
    }

    public String getQuestion_text2(){
        return question[1];
    }
}
