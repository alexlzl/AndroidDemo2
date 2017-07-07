package com.example.liuzhouliang.project;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class Main2Activity extends AppCompatActivity {
    /**
     * add branch44==
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        TextView textView= (TextView) findViewById(R.id.text2);
        textView.setText("taskid==="+getTaskId());
    }
}
