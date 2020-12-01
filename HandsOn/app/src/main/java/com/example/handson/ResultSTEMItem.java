package com.example.handson;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;

import androidx.annotation.Nullable;

public class ResultSTEMItem extends ResultItem {
    public ResultSTEMItem(Context context) {
        super(context);
        init(context);
    }

    public ResultSTEMItem(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context){
        LayoutInflater inflater =(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.survey_stem_result_item,this,true);
    }
}
