package com.example.chetan.waterlevelapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;


public class MainActivity extends AppCompatActivity {
    private Button otherTankButton;
    private Button sprinklerButton;
    private Button stopButton;
    private TextView waterLevelDisplay;
    private EditText temp;
    private String action="";
    public URL url;
    public Button viewGraphButton;
    int level;
    final int capacity = 1000;
    int threshhold = 800;
    boolean flag=true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        otherTankButton = (Button) findViewById(R.id.other_tank);
        sprinklerButton = (Button) findViewById(R.id.sprinklers);
        stopButton = (Button) findViewById(R.id.stop);
        waterLevelDisplay = (TextView) findViewById(R.id.water_level);
        temp = (EditText) findViewById(R.id.temp);
        viewGraphButton = (Button) findViewById(R.id.view_graph);
        try {
            url = new URL("http://project.coen396chetan.info/distance.php");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        new PostClass().execute();
        otherTankButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                action = "t";
                try {
                    url = new URL("http://project.coen396chetan.info/index.php?action=" + action);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                flag = false;
                Toast.makeText(MainActivity.this, "Reserve tank has been turned on", Toast.LENGTH_SHORT).show();
                new PostClass().execute();
                otherTankButton.setEnabled(false);
                sprinklerButton.setEnabled(false);
                stopButton.setEnabled(false);

            }
        });

        sprinklerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                action = "s";
                try {
                    url = new URL("http://project.coen396chetan.info/index.php?action="+action);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                flag = false;
                Toast.makeText(MainActivity.this, "Sprinkler has been turned on", Toast.LENGTH_SHORT).show();
                new PostClass().execute();
                otherTankButton.setEnabled(false);
                sprinklerButton.setEnabled(false);
                stopButton.setEnabled(false);
            }
        });

        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                action = "stop";
                try{
                    SmsManager sms = SmsManager.getDefault();
                    sms.sendTextMessage("4085067024", null, "Tank overflowing.. Please stop water supply", null, null);
                    Toast.makeText(MainActivity.this, "Message sent", Toast.LENGTH_SHORT).show();
                }catch(Exception e)
                {
                    Toast.makeText(MainActivity.this, "Unable to send message.", Toast.LENGTH_SHORT).show();
                }
                otherTankButton.setEnabled(false);
                sprinklerButton.setEnabled(false);
                stopButton.setEnabled(false);
//                Intent intent = new Intent(Intent.ACTION_VIEW);
//                intent.putExtra("sms_body", "default context");
//                intent.setType("vnd.android-dir/mms-sms");
                //MainActivity.this.startActivity(intent);
            }
        });

        viewGraphButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            startActivity(new Intent(MainActivity.this, GraphActivity.class));

            }
        });

    }
    private class PostClass extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {
            try {

                final TextView outputView = (TextView) findViewById(R.id.water_level);


                HttpURLConnection connection = (HttpURLConnection)url.openConnection();
                String urlParameters = "action="+action;
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);
                DataOutputStream dStream = new DataOutputStream(connection.getOutputStream());
                dStream.writeBytes(urlParameters);
                dStream.flush();
                dStream.close();
                final int responseCode = connection.getResponseCode();
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                if(flag) {
                    String line = "";
                    final StringBuilder responseOutput = new StringBuilder();
                    while ((line = br.readLine()) != null) {
                        responseOutput.append(line);
                    }
                    br.close();


                    MainActivity.this.runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            String[] output = {};
                            output = responseOutput.toString().split(" ");
                            String waterLevel = output[0];

                             int waterLevelInt = Integer.parseInt(waterLevel);
                             int result = waterLevelInt;
                            //waterLevelDisplay.setText("Current water level is:" + result + "%");
                            temp.setText("Current water level is: "+result+" units");
                            Toast.makeText(MainActivity.this, "Water level is : " + Integer.parseInt(waterLevel), Toast.LENGTH_SHORT).show();
                            if(waterLevelInt>threshhold){
                                otherTankButton.setEnabled(true);
                                sprinklerButton.setEnabled(true);
                                stopButton.setEnabled(true);
                            }
                        }
                    });
                }

            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return null;
        }




    }


}
