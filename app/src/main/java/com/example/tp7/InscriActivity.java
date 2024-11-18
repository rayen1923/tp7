package com.example.tp7;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InscriActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscri);
        Button Btnr = findViewById(R.id.Btnr);
        Btnr.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                EditText FirstNameEditText = findViewById(R.id.Txtln);
                EditText LastNameEditText = findViewById(R.id.Txtfn);
                EditText EmailEditText = findViewById(R.id.Txtm);
                EditText PasswordEditText = findViewById(R.id.Txtp);

                String firstname = FirstNameEditText.getText().toString().trim();
                String lastname = LastNameEditText.getText().toString().trim();
                String email = EmailEditText.getText().toString().trim();
                String password = PasswordEditText.getText().toString().trim();

                if (email.isEmpty() || password.isEmpty() || firstname.isEmpty() || lastname.isEmpty()) {
                    Toast.makeText(InscriActivity.this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
                    if (!isInternetAvailable()) {
                        Toast.makeText(InscriActivity.this, "No internet connection", Toast.LENGTH_LONG).show();
                        return;
                    }
                } else {
                    if (!isInternetAvailable()) {
                        Toast.makeText(InscriActivity.this, "No internet connection", Toast.LENGTH_LONG).show();
                        return;
                    }
                }

                User user = new User(email,firstname,lastname,password);

                ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
                Call<ServerResponse> call = apiService.registerUser(user);

                call.enqueue(new Callback<ServerResponse>() {
                    @Override
                    public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            String msg = response.body().GetMessage();
                            Toast.makeText(InscriActivity.this, msg, Toast.LENGTH_LONG).show();

                            if (response.body().getStatus().equals("success")) {
                                Intent intent = new Intent(InscriActivity.this, MainActivity.class);
                                intent.putExtra("email", email);
                                intent.putExtra("password", password);
                                startActivity(intent);
                                finish();
                            }
                        } else {
                            Toast.makeText(InscriActivity.this, "Registration error!", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ServerResponse> call, Throwable t) {
                        Toast.makeText(InscriActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
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