package com.example.tp7;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView reg = findViewById(R.id.register_now);
        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, InscriActivity.class);
                startActivity(intent);
            }
        });

        Button Btnr = findViewById(R.id.Btnl);
        Btnr.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                EditText EmailEditText = findViewById(R.id.Txtm);
                EditText PasswordEditText = findViewById(R.id.Txtp);

                String email = EmailEditText.getText().toString().trim();
                String password = PasswordEditText.getText().toString().trim();

                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!isInternetAvailable()) {
                    Toast.makeText(MainActivity.this, "No internet connection", Toast.LENGTH_LONG).show();
                    return;
                }

                ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
                LoginRequest loginRequest = new LoginRequest(email, password);
                Call<ServerResponse> call = apiService.getUser(loginRequest);

                call.enqueue(new Callback<ServerResponse>() {
                    @Override
                    public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            String msg = response.body().GetMessage();
                            Toast.makeText(MainActivity.this, msg, Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(MainActivity.this,HomeActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(MainActivity.this, "Login error!", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ServerResponse> call, Throwable t) {
                        Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    private boolean isInternetAvailable() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }
}
