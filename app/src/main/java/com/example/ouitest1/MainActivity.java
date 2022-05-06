package com.example.ouitest1;

import android.widget.EditText;
import android.widget.Toast;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void getURL(View view) {
        EditText editText = (EditText) findViewById(R.id.url);
        String url = editText.getText().toString();

        Toast toast = Toast.makeText(this, "Loading: " + url, Toast.LENGTH_SHORT);
        toast.show();
    }

}