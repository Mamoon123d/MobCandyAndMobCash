package com.cashhub.android.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.cashhub.android.BuildConfig;
import com.cashhub.android.Interface.GetApiData;
import com.cashhub.android.Models.UserSignUpModel;
import com.cashhub.android.R;
import com.cashhub.android.Services.RetrofitClient;
import com.cashhub.android.Services.sharedConstant;

import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserLogin extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_login);
        loginEmail();
        //  setLogin
    }

    private void loginEmail() {
        TextView loginBt = findViewById(R.id.loginBt);
        EditText emailEt = findViewById(R.id.emailEt);
        EditText passEt = findViewById(R.id.passEt);

        loginBt.setOnClickListener(v -> {
            String email = emailEt.getText().toString();
            String pass = passEt.getText().toString();
            if (email.isEmpty()) {
                emailEt.setError("Please Enter Email Id");
            } else if (!email.matches(String.valueOf(emailRegex))) {
                emailEt.setError("Please Enter valid Email Id");
            } else if (pass.isEmpty()) {
                passEt.setError("Please Enter Password");
            } else if (pass.length() < 6) {
                passEt.setError("Password should be more than 5 chars");
            } else {
                hitLogin(email, pass);
            }
        });
    }

    private void hitLogin(String email, String pass) {
        GetApiData getApiData = RetrofitClient.getRetrofitInstance().create(GetApiData.class);
        Call<UserSignUpModel> call = getApiData.getUserLogin(email, pass, BuildConfig.VERSION_NAME, BuildConfig.VERSION_CODE);
        ProgressDialog dialog = new ProgressDialog(this);
        TextView errorTv = findViewById(R.id.errorTv);
        dialog.setMessage("please wait...");
        dialog.show();
        call.enqueue(new Callback<UserSignUpModel>() {
            @Override
            public void onResponse(@NonNull Call<UserSignUpModel> call, @NonNull Response<UserSignUpModel> response) {
                if (response.isSuccessful()) {
                    UserSignUpModel data = response.body();
                    if (data != null) {
                        dialog.dismiss();
                        if (data.getMessage().matches("Success")) {

                            int userId = response.body().getUserId();
                            String securityToken = response.body().getSecurityToken();
                            sharedConstant.setSharedPreferenceInt(UserLogin.this, "userId", userId);
                            sharedConstant.setSharedPreferenceString(UserLogin.this, "securitytoken", securityToken);
                            sharedConstant.setSharedPreferenceString(UserLogin.this, "versionName", BuildConfig.VERSION_NAME);
                            sharedConstant.setSharedPreferenceInt(UserLogin.this, "versionCode", BuildConfig.VERSION_CODE);
                            startActivity(new Intent(UserLogin.this, MainActivity.class));
                            finish();

                        }
                        errorTv.setText(data.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<UserSignUpModel> call, @NonNull Throwable t) {
                dialog.dismiss();
            }
        });

    }

    public static final Pattern emailRegex = Pattern.compile(
            "[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?");


}
