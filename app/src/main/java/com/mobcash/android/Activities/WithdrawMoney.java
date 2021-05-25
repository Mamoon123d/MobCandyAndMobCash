package com.mobcash.android.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.mobcash.android.Models.WalletRedeemDataModel;
import com.mobcash.android.Interface.GetApiData;
import com.mobcash.android.R;
import com.mobcash.android.Services.RetrofitClient;
import com.mobcash.android.Services.sharedConstant;

import java.util.ArrayList;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WithdrawMoney extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    TextView Coins, Amount;
    Spinner spinner;
    LinearLayout WithdrawMoney;
    EditText Email, MobileNumber;
    ArrayList<String> SelectAmountArrayList;
    private String SelectAmountSpinner, redeemType = "Paytm";
    ProgressDialog progressDialog;
    private String TAG = "testing";
    private static final String PHONE_REGEX = "\\d{10}";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_withdraw_money);

            Coins = findViewById(R.id.totalCoins);
            Coins.setText(sharedConstant.getSharedPreferenceString(WithdrawMoney.this, "Coins", ""));

            final String mimeType = "text/html";
            final String encoding = "UTF-8";
            String html = sharedConstant.getSharedPreferenceString(WithdrawMoney.this, "transText", "");
            WebView webView = (WebView) findViewById(R.id.transText);
            webView.loadData(html, mimeType, encoding);


            Amount = findViewById(R.id.totalAmount);
            WithdrawMoney = findViewById(R.id.withdrawMoney);
            Email = findViewById(R.id.emailId);
            MobileNumber = findViewById(R.id.mobile);
            spinner = findViewById(R.id.spinner);

            Intent intent = getIntent();
            if (intent != null) {
                SelectAmountArrayList = intent.getStringArrayListExtra("payoutAmount");
                assert SelectAmountArrayList != null;
                Log.e("testing", "onCreate: " + SelectAmountArrayList.size());

            }


            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(WithdrawMoney.this, android.R.layout.simple_list_item_1, SelectAmountArrayList);

            dataAdapter.setDropDownViewResource(R.layout.simple_dropdown_item_1line);
            spinner.setAdapter(dataAdapter);
            spinner.setFocusable(true);
            spinner.setFocusableInTouchMode(true);
            spinner.requestFocus();


            Coins.setText(sharedConstant.getSharedPreferenceString(WithdrawMoney.this, "Coins", ""));

            Amount.setText(sharedConstant.getSharedPreferenceString(WithdrawMoney.this, "Currency", "") + " " + sharedConstant.getSharedPreferenceString(com.mobcash.android.Activities.WithdrawMoney.this, "Amount", ""));


            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            toolbar.setTitle("Withdraw Coins");
            toolbar.setNavigationIcon(R.drawable.ic_back);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });

            WithdrawMoney.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    invalidateData();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Log.w(TAG, "onCreate: WithdrawMoney" + e);
        }

    }

    private void invalidateData() {

        String emailaddress = Email.getText().toString();
        String moblienumber = MobileNumber.getText().toString();
        SelectAmountSpinner = spinner.getSelectedItem().toString();
        Log.e("testing", "invalidateData: " + SelectAmountSpinner);

        if (SelectAmountSpinner.equals("Select Amount")) {
            TextView errorText = (TextView) spinner.getSelectedView();
            errorText.setTextColor(Color.RED);//just to highlight that this is an error
            errorText.setText("Please Select Amount");
            spinner.requestFocus();
            return;
        }
        if (emailaddress.isEmpty()) {
            Email.setError("Email Id Required");
            Email.requestFocus();
            return;
        }
        if (isValidPhone(moblienumber))
        {
            MobileNumber.setError("Enter correct 10 digits paytm number");
            MobileNumber.requestFocus();
            return;
        } else {
            walletRedeemData(moblienumber, emailaddress, SelectAmountSpinner, redeemType);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        SelectAmountSpinner = spinner.getSelectedItem().toString();
        Log.e("testing", "invalidateData: " + SelectAmountSpinner);

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        Log.e("testing", "onNothingSelected: ");

    }

    private void walletRedeemData(String moblienumber, String emailaddress, String selectAmountSpinner, String redeemType) {

        GetApiData getApiData = RetrofitClient.getRetrofitInstance().create(GetApiData.class);
        Call<WalletRedeemDataModel> call = getApiData.sendWithdrawCoinsData(sharedConstant.getSharedPreferenceInt(com.mobcash.android.Activities.WithdrawMoney.this, "userId", 0)
                , sharedConstant.getSharedPreferenceString(com.mobcash.android.Activities.WithdrawMoney.this, "securitytoken", ""),
                sharedConstant.getSharedPreferenceString(com.mobcash.android.Activities.WithdrawMoney.this, "versionName", ""),
                sharedConstant.getSharedPreferenceInt(com.mobcash.android.Activities.WithdrawMoney.this, "versionCode", 0), moblienumber, emailaddress, selectAmountSpinner, redeemType);


        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.loadingwait));
        progressDialog.show();
        progressDialog.setCancelable(false);

        call.enqueue(new Callback<WalletRedeemDataModel>() {

            @Override
            public void onResponse(Call<WalletRedeemDataModel> call, Response<WalletRedeemDataModel> response) {
                dismissProgressDialog();

                if (response != null) {


                    if (response.isSuccessful()) {
                        assert response.body() != null;
                        if (response.body().getStatus() == 200) {

                            String coins = String.valueOf(response.body().getUserCoin());
                            String amount = String.valueOf(response.body().getUserAmount());
                            String currency = String.valueOf(response.body().getCurrency());

                            sharedConstant.setSharedPreferenceString(com.mobcash.android.Activities.WithdrawMoney.this, "Coins", coins);
                            sharedConstant.setSharedPreferenceString(com.mobcash.android.Activities.WithdrawMoney.this, "Amount", amount);
                            sharedConstant.setSharedPreferenceString(com.mobcash.android.Activities.WithdrawMoney.this, "Currency", currency);

                            Toast.makeText(com.mobcash.android.Activities.WithdrawMoney.this, "" + response.body().getShowText(), Toast.LENGTH_SHORT).show();
                            onBackPressed();

                        } else {
                            Toast.makeText(WithdrawMoney.this, "System Message: " + response.errorBody(), Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    Toast.makeText(WithdrawMoney.this, "System Message: " + response.errorBody(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<WalletRedeemDataModel> call, Throwable t) {
                dismissProgressDialog();
//                Toast.makeText(WithdrawMoney.this, getString(R.string.systemmessage) +t, Toast.LENGTH_SHORT).show();

            }
        });

    }

    private boolean isValidPhone(String phone)
    {
        boolean check=false;
        if(!Pattern.matches("[a-zA-Z]+", phone))
        {
            if(phone.length() > 9)
            {
                check = false;

            }
            else
            {
                check = true;

            }
        }
        else
        {
            check=false;
        }
        return check;
    }

    private void dismissProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        dismissProgressDialog();

    }

    @Override
    public void onStop() {
        super.onStop();
        dismissProgressDialog();

    }

}
