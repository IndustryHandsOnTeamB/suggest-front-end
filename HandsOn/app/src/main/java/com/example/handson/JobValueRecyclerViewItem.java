package com.example.handson;

public class JobValueRecyclerViewItem {

    //question에 가치관 2개를 넣기
    private String[] question;
    private int mstbVal;


    public JobValueRecyclerViewItem(String[] question){
        this.question = question;
        this.mstbVal = -1; // 초기화
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

    public String getQuestion_text1() {
        return question[0];
    }

    public String getQuestion_text2(){
        return question[1];
    }
}
