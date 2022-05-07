package com.example.ouitest1;

import android.widget.EditText;
import android.widget.Toast;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void getURL(View view) {
        EditText editText = (EditText) findViewById(R.id.url_text);
        String url_str = editText.getText().toString();

        Toast toast = Toast.makeText(this, "Loading: " + url_str, Toast.LENGTH_SHORT);
        toast.show();

        URL url = null;
        try {
            url = new URL(url_str);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        HttpURLConnection urlConnection = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setConnectTimeout(60);
        } catch (IOException e) {
            e.printStackTrace();
        }

        int response_code = 0;
        try {
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            response_code = urlConnection.getResponseCode();
            toast.setText("Response: " + response_code);
        } catch (IOException e) {
            toast.setText("Error");
            e.printStackTrace();
        }
        toast.show();
    }

}