package com.example.ouitest1;

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
        Toast toast = Toast.makeText(this, "Test", Toast.LENGTH_SHORT);
        toast.show();
    }

}