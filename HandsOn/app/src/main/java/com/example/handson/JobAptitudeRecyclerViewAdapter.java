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

import java.lang.reflect.Array;
import java.util.ArrayList;

public class JobAptitudeRecyclerViewAdapter extends RecyclerView.Adapter<JobAptitudeRecyclerViewAdapter.ViewHolder> {

    private ArrayList<JobAptitudeRecyclerViewItem> arrayListQuestion;

    public JobAptitudeRecyclerViewAdapter(ArrayList<JobAptitudeRecyclerViewItem> arrayListQuestion){
        this.arrayListQuestion = arrayListQuestion;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        Context context= parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View jobAptView = inflater.inflate(R.layout.survey_job_apt_item, parent, false);
        //ViewHolder viewHolder = new ViewHolder(jobAptView);

        return new ViewHolder(jobAptView);
    }

    @NonNull

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        String question = arrayListQuestion.get(holder.getAdapterPosition()).getQuestion();

        TextView textQuestion = holder.textQuestion;
        textQuestion.setText(question);

        CharSequence[] values = new CharSequence[]{"1","2","3","4","5","6","7"};
        //holder.mstb_btn.setElements(values);


        holder.mstb_btn.setOnValueChangedListener(new ToggleButton.OnValueChangedListener() {
            @Override
            public void onValueChanged(int value) {
                if(position == 0){
                    Log.d("TAG", "onValueChanged: 0 - 1번째 문항이 눌렸다.");
                    int a = holder.mstb_btn.getValue();
                    Log.d("TAG", "onValueChanged: 그 값은 " + Integer.toString(a));

                }
                else if(position == 1){
                    Log.d("TAG", "onValueChanged: 1");
                    int a = holder.mstb_btn.getValue();
                    Log.d("TAG", "onValueChanged: 그 값은 " + Integer.toString(a));
                }
                else if(position == 2){
                    Log.d("TAG", "onValueChanged: 2");
                    int a = holder.mstb_btn.getValue();
                    Log.d("TAG", "onValueChanged: 그 값은 " + Integer.toString(a));
                }
                else if(position == 3){
                    Log.d("TAG", "onValueChanged: 3");
                    int a = holder.mstb_btn.getValue();
                    Log.d("TAG", "onValueChanged: 그 값은 " + Integer.toString(a));
                }
                else if(position == 4){
                    Log.d("TAG", "onValueChanged: 4");
                    int a = holder.mstb_btn.getValue();
                    Log.d("TAG", "onValueChanged: 그 값은 " + Integer.toString(a));
                }
                else if(position == 5){
                    Log.d("TAG", "onValueChanged: 5");
                    int a = holder.mstb_btn.getValue();
                    Log.d("TAG", "onValueChanged: 그 값은 " + Integer.toString(a));
                }
                else if(position == 6){
                    Log.d("TAG", "onValueChanged: 6");
                    int a = holder.mstb_btn.getValue();
                    Log.d("TAG", "onValueChanged: 그 값은 " + Integer.toString(a));
                }
                else if(position == 7){
                    Log.d("TAG", "onValueChanged: 7");
                    int a = holder.mstb_btn.getValue();
                    Log.d("TAG", "onValueChanged: 그 값은 " + Integer.toString(a));
                }
                else if(position == 8){
                    Log.d("TAG", "onValueChanged: 8");
                    int a = holder.mstb_btn.getValue();
                    Log.d("TAG", "onValueChanged: 그 값은 " + Integer.toString(a));
                }
                else if(position == 9){
                    Log.d("TAG", "onValueChanged: 9");
                    int a = holder.mstb_btn.getValue();
                    Log.d("TAG", "onValueChanged: 그 값은 " + Integer.toString(a));
                }
                else{
                    Log.d("TAG", "onValueChanged: 10");
                    int a = holder.mstb_btn.getValue();
                    Log.d("TAG", "onValueChanged: 그 값은 " + Integer.toString(a));
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return arrayListQuestion.size();
    }


    protected class ViewHolder extends RecyclerView.ViewHolder{

        View itemView;
        TextView textQuestion;
        MultiStateToggleButton mstb_btn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textQuestion = (TextView)itemView.findViewById(R.id.text_survey_job_apt_question);
            mstb_btn = (MultiStateToggleButton)itemView.findViewById(R.id.mstb_btn_jobapt);
            this.itemView = itemView;
        }

        View viewReturn(){
            return itemView;
        }

        public MultiStateToggleButton viewReturnMstb(){
            return this.mstb_btn;
        }
    }

}
