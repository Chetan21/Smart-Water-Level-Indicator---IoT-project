package com.example.chetan.waterlevelapp;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by Chetan on 3/18/2016.
 */
public class InfoActivity extends Activity {
    private EditText ETPhone;
    private EditText ETDepth;
    private Button submit;
    String phone = "";
    int depth = 0;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        ETDepth = (EditText) findViewById(R.id.tank_depth);
        ETPhone = (EditText) findViewById(R.id.phone_number);
        submit = (Button) findViewById(R.id.submit);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                depth = Integer.parseInt(ETDepth.getText().toString());
                phone = ETPhone.getText().toString();

                SharedPreferences phonePref = getSharedPreferences(phone, Context.MODE_PRIVATE);
                SharedPreferences depthPref = getSharedPreferences(depth+"", Context.MODE_PRIVATE);

            }
        });

    }
}
