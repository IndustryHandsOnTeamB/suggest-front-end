package com.example.handson;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;

import androidx.annotation.Nullable;

public class ResultJobValueItem extends ResultItem {
    public ResultJobValueItem(Context context) {
        super(context);
        init(context);
    }

    public ResultJobValueItem(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context){
        LayoutInflater inflater =(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.survey_job_value_result_item,this,true);
    }
}
