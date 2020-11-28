package com.example.handson;

import android.content.Context;
import android.graphics.Color;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.honorato.multistatetogglebutton.MultiStateToggleButton;
import org.honorato.multistatetogglebutton.ToggleButton;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;

public class JobValueRecyclerViewAdapter extends RecyclerView.Adapter<JobValueRecyclerViewAdapter.ViewHolder>{

    private ArrayList<JobValueRecyclerViewItem> arrayListQuestion;

    public JobValueRecyclerViewAdapter(ArrayList<JobValueRecyclerViewItem> arrayListQuestion) {
        this.arrayListQuestion = arrayListQuestion;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View JobValueView = inflater.inflate(R.layout.survey_job_value_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(JobValueView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        String question = "- 둘 중 자신에게 더 중요한 가치를 선택하세요.";

        TextView textQuestion = holder.textQuestion;
        textQuestion.setText(question);

//        String question = arrayListQuestion.get(holder.getAdapterPosition()).getQuestion();

        CharSequence text1 = arrayListQuestion.get(holder.getAdapterPosition()).getQuestion_text1();
        CharSequence text2 = arrayListQuestion.get(holder.getAdapterPosition()).getQuestion_text2();
        CharSequence[] values = new CharSequence[]{text1, text2};
        holder.mstb_btn.setElements(values);

        holder.mstb_btn.setOnValueChangedListener(new ToggleButton.OnValueChangedListener() {
            @Override
            public void onValueChanged(int position) {
                if (position == 0){
                    Log.d("TAG", "onValueChanged: 0");
                    //intent.putExtra("choice",1);
                    //btn1 = true;
                }
                else{
                    Log.d("TAG", "onValueChanged: 1");
                    //intent.putExtra("choice",2);
                    //btn1 = true;
                }
            }
        });
        holder.mstb_btn.getValue();

    }

    @Override
    public int getItemCount() {
        return arrayListQuestion.size();
    }


    //ViewHolder 정의
    protected class ViewHolder extends RecyclerView.ViewHolder{

        TextView textQuestion;
        MultiStateToggleButton mstb_btn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textQuestion = (TextView)itemView.findViewById(R.id.text_survey_job_value_question);
            mstb_btn = (MultiStateToggleButton)itemView.findViewById(R.id.mstb_btn);
            }
    }

}
