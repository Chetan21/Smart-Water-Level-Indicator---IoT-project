package com.example.chetan.waterlevelapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class GraphActivity extends AppCompatActivity {

    public URL url;
    Boolean flag = true;
    private String action="";
    ArrayList<String> waterLevel = new ArrayList<>();
    ArrayList<String> dates = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        try {
            url = new URL("http://project.coen396chetan.info/graph.php");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        new PostClass().execute();


    }

    private class PostClass extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {
            try {

//                final TextView outputView = (TextView) findViewById(R.id.water_level);


                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                String urlParameters = "action=" + action;
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);
                DataOutputStream dStream = new DataOutputStream(connection.getOutputStream());
                dStream.writeBytes(urlParameters);
                dStream.flush();
                dStream.close();
                final int responseCode = connection.getResponseCode();
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                if (flag) {
                    String line = "";
                    final StringBuilder responseOutput = new StringBuilder();
                    while ((line = br.readLine()) != null) {
                        responseOutput.append(line);
                    }
                    br.close();


                    GraphActivity.this.runOnUiThread(new Runnable() {

                        @Override
                        public void run() {

                            String[] output = {};
                            output = responseOutput.toString().split("#");

                            Toast.makeText(getApplicationContext(), output.length+ "", Toast.LENGTH_SHORT).show();


                            for(int i=0; i<output.length; i++)
                            {
                                if(i%2==0)
                                {
                                    waterLevel.add(output[i]);
                                }
                                else
                                {
                                    dates.add(output[i]);
                                }
                            }

                            String[] waterLevelArray = new String[waterLevel.size()];
                            waterLevelArray = waterLevel.toArray(waterLevelArray);

                            String[] datesArray = new String[dates.size()];
                            datesArray = waterLevel.toArray(datesArray);

                            Bundle b=new Bundle();
                            b.putStringArray("waterlevel", waterLevelArray);
                            b.putStringArray("dates", datesArray);
                            Intent i=new Intent(getApplicationContext(), SimpleXYPlotActivity.class);
                            Toast.makeText(getApplicationContext(), "w" + waterLevel.size() + "d" + dates.size(), Toast.LENGTH_SHORT).show();

                            i.putExtras(b);
                            startActivity(i);

//                            int waterLevelInt = Integer.parseInt(waterLevel);
//                            double result = waterLevelInt * 0.1;
                            //waterLevelDisplay.setText("Current water level is:" + result + "%");
                            // temp.setText("Current water level is: " + result + "%");
//                            Toast.makeText(MainActivity.this, "Water level is : " + Integer.parseInt(waterLevel), Toast.LENGTH_SHORT).show();
//                            if (waterLevelInt > threshhold) {
//                                otherTankButton.setEnabled(true);
//                                sprinklerButton.setEnabled(true);
//                                stopButton.setEnabled(true);
//                            }
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
