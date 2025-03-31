package com.example.medilink_app;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;


public class MainActivity extends AppCompatActivity {

    private ImageView arrowImageView;
    private TextView getStartedText;

    TextView connectionStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        arrowImageView = findViewById(R.id.arrowImageView);
        getStartedText = findViewById(R.id.getStartedText);

        arrowImageView.setOnTouchListener(new SwipeGestureDetector(this, arrowImageView) {
            @Override
            public void onSwipeRight() {
                // Convert 250dp to pixels
                float distance = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 250, getResources().getDisplayMetrics());

                // Animate the arrow
                ObjectAnimator animator = ObjectAnimator.ofFloat(arrowImageView, "translationX", 0f, distance);
                animator.setDuration(300);
                animator.start();

                animator.addListener(new android.animation.AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(android.animation.Animator animation) {
                        getStartedText.setVisibility(TextView.INVISIBLE);
                        startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    }
                });
            }
        });

        connectionStatus = findViewById(R.id.connectionStatus);
        checkDatabaseConnection();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Reset the arrow's position and make the "Get Started" text visible
        arrowImageView.setTranslationX(0f);
        getStartedText.setVisibility(TextView.VISIBLE);
    }


    public void checkDatabaseConnection() {
       String url = "https://172.16.71.225/android_api/connect.php";
//        String url = "https://192.168.8.41/android_api/connect.php";

        // Bypass SSL certificate validation
        try {
            TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return null;
                        }

                        public void checkClientTrusted(
                                java.security.cert.X509Certificate[] certs, String authType) {
                        }

                        public void checkServerTrusted(
                                java.security.cert.X509Certificate[] certs, String authType) {
                        }
                    }
            };

            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

            HostnameVerifier allHostsValid = new HostnameVerifier() {
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            };

            HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            e.printStackTrace();
        }

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onResponse(String response) {
                        try {
                            // Log the response for debugging
                            Log.d("DatabaseResponse", response);

                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");
                            String message = jsonObject.getString("message");

                            connectionStatus.setText(message);

                            if (status.equals("success")) {
                                Toast.makeText(MainActivity.this, "Connected to database", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(MainActivity.this, "Database Connection Failed: " + message, Toast.LENGTH_LONG).show();
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            connectionStatus.setText("Error: Database Connection in Web service");
                            Toast.makeText(MainActivity.this, "Database Connection Failed", Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        connectionStatus.setText("Error: Database Connection in your app has an error");
                        error.printStackTrace();
                        Toast.makeText(MainActivity.this, "Network Error: " + error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });

        queue.add(request);
    }
}