package com.example.ouitest1;

import android.widget.EditText;
import android.widget.Toast;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import okhttp3.*;

import java.io.IOException;

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

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            public void onResponse(Call call, Response response) throws IOException {
                try (ResponseBody responseBody = response.body()) {
                    if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                    Headers responseHeaders = response.headers();
                    for (int i = 0, size = responseHeaders.size(); i < size; i++) {
                        System.out.println(responseHeaders.name(i) + ": " + responseHeaders.value(i));
                    }

                    toast.setText(responseBody.string());
                    toast.show();
                }
            }
        });

    }
}