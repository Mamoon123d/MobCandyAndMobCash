package com.cashhub.android.Activities;

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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import com.cashhub.android.Interface.GetApiData;
import com.cashhub.android.Models.WalletRedeemDataModel;
import com.cashhub.android.R;
import com.cashhub.android.Services.RetrofitClient;
import com.cashhub.android.Services.sharedConstant;
import com.squareup.picasso.Picasso;

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
    private RadioButton payType;
    private ImageView payIV;
    private int id;
    private CardView phoneCard, playerIdCard;
    private EditText phoneEt, playerEt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraw_money);
        phoneCard = findViewById(R.id.phoneCard);
        playerIdCard = findViewById(R.id.playerIdCard);
        phoneEt = findViewById(R.id.mobile);
        playerEt = findViewById(R.id.playerEt);

        try {

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
            payType = findViewById(R.id.payType);
            payIV = findViewById(R.id.payIv);


            Intent intent = getIntent();

            if (intent != null) {
                SelectAmountArrayList = intent.getStringArrayListExtra("payoutAmount");
                assert SelectAmountArrayList != null;
                Log.e("testing", "onCreate: " + SelectAmountArrayList.size());
                String title = intent.getStringExtra("rewardTitle");
                id = intent.getIntExtra("rewardId", 0);
                String payImg = intent.getStringExtra("rewardImage");
                Picasso.get().load(payImg).placeholder(getDrawable(R.drawable.gold_coins)).into(payIV);
                payType.setText(title);

                if (id == 1 || id == 4) {
                    playerIdCard.setVisibility(View.GONE);
                    phoneCard.setVisibility(View.VISIBLE);
                    if (id == 1) {
                        phoneEt.setHint("Enter Paytm Number");
                    } else {
                        phoneEt.setHint("Enter Phone Number");
                    }
                } else {
                    playerIdCard.setVisibility(View.VISIBLE);
                    phoneCard.setVisibility(View.GONE);
                }

            }


            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(WithdrawMoney.this, android.R.layout.simple_list_item_1, SelectAmountArrayList);

            dataAdapter.setDropDownViewResource(R.layout.simple_dropdown_item_1line);
            spinner.setAdapter(dataAdapter);
            spinner.setFocusable(true);
            spinner.setFocusableInTouchMode(true);
            spinner.requestFocus();


            Coins.setText(sharedConstant.getSharedPreferenceString(WithdrawMoney.this, "Coins", ""));

            Amount.setText(sharedConstant.getSharedPreferenceString(WithdrawMoney.this, "Currency", "") + " " + sharedConstant.getSharedPreferenceString(com.cashhub.android.Activities.WithdrawMoney.this, "Amount", ""));


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
                    invalidateData(id);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Log.w(TAG, "onCreate: WithdrawMoney" + e);
        }

    }

    private void invalidateData(int id) {

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
        /*if (id == 1 || id == 4) {
            if (id == 1)
                MobileNumber.setError("Paytm Number Required");
            else
                MobileNumber.setError("Mobile Number Required");

        } else {

        }*/
        if (isValidPhone(moblienumber) && (id == 1 || id == 4)) {
            MobileNumber.setError("Enter correct 10 digits paytm number");
            MobileNumber.requestFocus();
        } else if (!(id == 1 || id == 4) && playerEt.getText().toString().trim().isEmpty()) {
            playerEt.setError("Enter Player Id");
            playerEt.requestFocus();
        } else {
            walletRedeemData(id == 1 || id == 4 ? moblienumber : playerEt.getText().toString(), emailaddress, SelectAmountSpinner, redeemType);
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
        Call<WalletRedeemDataModel> call = getApiData.sendWithdrawCoinsData(sharedConstant.getSharedPreferenceInt(com.cashhub.android.Activities.WithdrawMoney.this, "userId", 0)
                , sharedConstant.getSharedPreferenceString(com.cashhub.android.Activities.WithdrawMoney.this, "securitytoken", ""),
                sharedConstant.getSharedPreferenceString(com.cashhub.android.Activities.WithdrawMoney.this, "versionName", ""),
                sharedConstant.getSharedPreferenceInt(com.cashhub.android.Activities.WithdrawMoney.this, "versionCode", 0), moblienumber, emailaddress, selectAmountSpinner, redeemType);


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

                            sharedConstant.setSharedPreferenceString(com.cashhub.android.Activities.WithdrawMoney.this, "Coins", coins);
                            sharedConstant.setSharedPreferenceString(com.cashhub.android.Activities.WithdrawMoney.this, "Amount", amount);
                            sharedConstant.setSharedPreferenceString(com.cashhub.android.Activities.WithdrawMoney.this, "Currency", currency);

                            Toast.makeText(com.cashhub.android.Activities.WithdrawMoney.this, "" + response.body().getShowText(), Toast.LENGTH_SHORT).show();
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
