package com.example.handson;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class STEMRecyclerViewAdapter extends RecyclerView.Adapter<STEMRecyclerViewAdapter.ViewHolder> {

    private ArrayList<STEMRecyclerViewItem> arrayListQuestion;

    public STEMRecyclerViewAdapter(ArrayList<STEMRecyclerViewItem> arrayListQuestion) {
        this.arrayListQuestion = arrayListQuestion;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater =  LayoutInflater.from(context);

        View STEMView = inflater.inflate(R.layout.survey_stem_major_suitability_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(STEMView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        String question = arrayListQuestion.get(holder.getAdapterPosition()).getQuestion();

        TextView textQuestion = holder.textQuestion;
        textQuestion.setText(question);

        holder.buttonAnswer0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.getAdapterPosition()!=RecyclerView.NO_POSITION){
                    holder.setButtonClear();

                    arrayListQuestion.get(holder.getAdapterPosition()).setButtonClear();
                    arrayListQuestion.get(holder.getAdapterPosition()).setCheckButton1(true);
                    holder.buttonAnswer0.setSelected(true);
                }
            }
        });

        holder.buttonAnswer1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.getAdapterPosition()!=RecyclerView.NO_POSITION) {
                    holder.setButtonClear();

                    arrayListQuestion.get(holder.getAdapterPosition()).setButtonClear();
                    arrayListQuestion.get(holder.getAdapterPosition()).setCheckButton2(true);
                    holder.buttonAnswer1.setSelected(true);
                }
            }
        });

        holder.buttonAnswer2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.getAdapterPosition()!=RecyclerView.NO_POSITION) {
                    holder.setButtonClear();

                    arrayListQuestion.get(holder.getAdapterPosition()).setButtonClear();
                    arrayListQuestion.get(holder.getAdapterPosition()).setCheckButton3(true);
                    holder.buttonAnswer2.setSelected(true);
                }
            }
        });

        holder.buttonAnswer3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.getAdapterPosition()!=RecyclerView.NO_POSITION) {
                    holder.setButtonClear();

                    arrayListQuestion.get(holder.getAdapterPosition()).setButtonClear();
                    arrayListQuestion.get(holder.getAdapterPosition()).setCheckButton4(true);
                    holder.buttonAnswer3.setSelected(true);
                }
            }
        });

        holder.buttonAnswer4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.getAdapterPosition()!=RecyclerView.NO_POSITION) {
                    holder.setButtonClear();

                    arrayListQuestion.get(holder.getAdapterPosition()).setButtonClear();
                    arrayListQuestion.get(holder.getAdapterPosition()).setCheckButton5(true);
                    holder.buttonAnswer4.setSelected(true);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayListQuestion.size();
    }

    protected class ViewHolder extends RecyclerView.ViewHolder{

        TextView textQuestion;
        Button buttonAnswer0;
        Button buttonAnswer1;
        Button buttonAnswer2;
        Button buttonAnswer3;
        Button buttonAnswer4;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textQuestion = (TextView)itemView.findViewById(R.id.text_survey_stem_major_suitability_question);
            buttonAnswer0 = (Button)itemView.findViewById(R.id.button_survey_stem_major_suitability_answer0);
            buttonAnswer1 = (Button)itemView.findViewById(R.id.button_survey_stem_major_suitability_answer1);
            buttonAnswer2 = (Button)itemView.findViewById(R.id.button_survey_stem_major_suitability_answer2);
            buttonAnswer3 = (Button)itemView.findViewById(R.id.button_survey_stem_major_suitability_answer3);
            buttonAnswer4 = (Button)itemView.findViewById(R.id.button_survey_stem_major_suitability_answer4);
        }

        public void setButtonClear(){
            buttonAnswer0.setSelected(false);
            buttonAnswer1.setSelected(false);
            buttonAnswer2.setSelected(false);
            buttonAnswer3.setSelected(false);
            buttonAnswer4.setSelected(false);
        }
    }
}
