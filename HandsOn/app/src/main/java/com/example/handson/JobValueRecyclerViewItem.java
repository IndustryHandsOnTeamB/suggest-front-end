package com.example.handson;

import org.honorato.multistatetogglebutton.MultiStateToggleButton;

public class JobValueRecyclerViewItem {

    //question에 가치 2개를 넣어놔야할 것 같다.
    private String[] question;
    MultiStateToggleButton mstb;

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
