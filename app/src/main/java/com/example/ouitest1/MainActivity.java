package com.example.ouitest1;

import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import ie.equalit.ouinet.Config;
import ie.equalit.ouinet.Ouinet;
import okhttp3.*;

import javax.net.ssl.*;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {
    private Ouinet ouinet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Config config = new Config.ConfigBuilder(this)
                .setCacheType("bep5-http")
                .setTlsCaCertStorePath("file:///android_asset/cacert.pem")
                .setCacheHttpPubKey(BuildConfig.CACHE_PUB_KEY)
                .setInjectorCredentials(BuildConfig.INJECTOR_CREDENTIALS)
                .setInjectorTlsCert(BuildConfig.INJECTOR_TLS_CERT)
                .setLogLevel(Config.LogLevel.DEBUG)
                //.setDisableOriginAccess(true)
                //.setDisableProxyAccess(true)
                //.setDisableInjectorAccess(true)
                .build();

        ouinet = new Ouinet(this, config);
        ouinet.start();

        Executors.newFixedThreadPool(1).execute((Runnable) this::updateServiceState);
    }

    private void updateServiceState() {
      TextView ouinetState = (TextView) findViewById(R.id.ouinetStatus);

      while(true)
      {
          try {
              Thread.sleep(1000);
          } catch (InterruptedException e) {
              e.printStackTrace();
          }

          String state = ouinet.getState().toString();
          runOnUiThread(() -> ouinetState.setText("State: " + state));
      }
    }

    public void getURL(View view) {
        EditText editText = (EditText) findViewById(R.id.url);
        TextView logViewer = (TextView) findViewById(R.id.log_viewer);
        String url = editText.getText().toString();

        Toast toast = Toast.makeText(this, "Loading: " + url, Toast.LENGTH_SHORT);
        toast.show();

        OkHttpClient client = getUnsafeHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                logViewer.setText(e.toString());
            }

            public void onResponse(Call call, Response response) throws IOException {
                try (ResponseBody responseBody = response.body()) {
                    Headers responseHeaders = response.headers();
                    for (int i = 0, size = responseHeaders.size(); i < size; i++) {
                        System.out.println(responseHeaders.name(i) + ": " + responseHeaders.value(i));
                    }

                    logViewer.setText(responseHeaders.toString());
                }
            }
        });
    }

    private OkHttpClient getUnsafeHttpClient() {
        try {
            TrustManager[] trustAllCerts = getTrustManager();
            SSLSocketFactory sslSocketFactory = getSSLSocketFactory(trustAllCerts);

            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.sslSocketFactory(
                    sslSocketFactory,
                    (X509TrustManager) trustAllCerts[0]);

            // Bypass hostname verification
            //builder.hostnameVerifier(new HostnameVerifier() {
            //    @Override
            //    public boolean verify(String hostname, SSLSession session) {
            //        return true;
            //    }
            //});

            // Proxy to ouinet service
            Proxy ouinetService= new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", 8077));
            builder.proxy(ouinetService);
            return builder.build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private SSLSocketFactory getSSLSocketFactory(TrustManager[] trustAllCerts) throws NoSuchAlgorithmException, KeyManagementException {
        // Install the all-trusting trust manager
        final SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
        return sslContext.getSocketFactory();
    }

    private TrustManager[] getTrustManager() {
        // Create a trust manager that does not validate certificate chains
        final TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {

                    @Override
                    public void checkClientTrusted(java.security.cert.X509Certificate[] chain,
                                                   String authType) {
                    }

                    @Override
                    public void checkServerTrusted(java.security.cert.X509Certificate[] chain,
                                                   String authType) {
                    }
                    @Override
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return new java.security.cert.X509Certificate[]{};
                    }
                }
        };
        return trustAllCerts;
    }
}