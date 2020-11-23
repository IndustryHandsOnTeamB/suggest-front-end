package com.example.handson;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class RecommendListActivity extends AppCompatActivity {

    ImageButton backButton;
    TextView recommendDepartmentText;
    TextView recommendJobText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommend_list);

        backButton = findViewById(R.id.button_recommend_back);
        recommendDepartmentText = findViewById(R.id.text_recommend_department);
        recommendJobText = findViewById(R.id.text_recommend_job);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
