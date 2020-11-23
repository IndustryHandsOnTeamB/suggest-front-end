package com.example.handson;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class CaseSearchActivity extends AppCompatActivity {

    EditText searchEdittext;
    Button searchButton;
    TextView scrollTextview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_case_search);

        searchEdittext = (EditText) findViewById(R.id.search_edittext);
        searchButton = (Button) findViewById(R.id.search_button);
        scrollTextview = (TextView) findViewById(R.id.scroll_tv);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scrollTextview.setText("sample text  sample text\\n sample text  sample text\\n\n" +
                        "sample text  sample text\\n sample text  sample text\\n sample text  sample text\\n sample text  sample text\\n sample text  sample text\\n\n" +
                        "sample text  sample text\\n sample text  sample text\\n sample text  sample text\\n sample text @\n" +
                        "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@\n" +
                        "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@\n");
            }
        });

    }
}
