package com.mobcash.android.Activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mobcash.android.Models.Transaction;
import com.mobcash.android.Models.UserTransactionModel;
import com.mobcash.android.Adapter.TransactionsAdapter;
import com.mobcash.android.Interface.GetApiData;
import com.mobcash.android.R;
import com.mobcash.android.Services.RetrofitClient;
import com.mobcash.android.Services.sharedConstant;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Transactions extends AppCompatActivity {

    RecyclerView transactionsRecycler;
    ArrayList<Transaction> TransactionArrayList = null;
    TransactionsAdapter transactionsAdapter = null;
    ProgressDialog progressDialog;
    String TAG = "testing";
    AVLoadingIndicatorView avLoadingIndicatorView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_transactions);

            transactionsRecycler = findViewById(R.id.transactionsRecycler);
            avLoadingIndicatorView = findViewById(R.id.loader_indicator);

            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            toolbar.setTitle("Transactions");
            toolbar.setNavigationIcon(R.drawable.ic_back);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });

            getTransactions();

        } catch (Exception e) {
            e.printStackTrace();
            Log.w(TAG, "onCreate: Trasactions"+e );
        }
    }

    private void getTransactions() {
         avLoadingIndicatorView.show();
        GetApiData getApiData = RetrofitClient.getRetrofitInstance().create(GetApiData.class);
        Call<UserTransactionModel> call = getApiData.getUserTransactions(sharedConstant.getSharedPreferenceInt(Transactions.this, "userId", 0)
                , sharedConstant.getSharedPreferenceString(Transactions.this, "securitytoken", "")
                , sharedConstant.getSharedPreferenceString(Transactions.this, "versionName", ""),
                sharedConstant.getSharedPreferenceInt(Transactions.this, "versionCode", 0));

//        if (!((Activity) Transactions.this).isFinishing()) {
//            progressDialog = new ProgressDialog(Transactions.this);
//            progressDialog.setMessage(getString(R.string.loadingwait));
//            progressDialog.show();
//            progressDialog.setCancelable(false);
//        }

        call.enqueue(new Callback<UserTransactionModel>() {

            @Override
            public void onResponse(Call<UserTransactionModel> call, Response<UserTransactionModel> response) {
                dismissProgressDialog();
                try {

                    if (response != null) {
                        assert response.body() != null;
                        if (response.body().getStatus() == 200) {

                            TransactionArrayList = response.body().getTransactions();

                            transactionsAdapter = new TransactionsAdapter(TransactionArrayList, Transactions.this);
                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(Transactions.this);
                            transactionsRecycler.setLayoutManager(mLayoutManager);
                            transactionsRecycler.setItemAnimator(new DefaultItemAnimator());
                            transactionsRecycler.setAdapter(transactionsAdapter);

                        } else
                            Toast.makeText(Transactions.this, "System Message: " + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(Transactions.this, "System Message: " + response.errorBody().toString(), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {

                    Toast.makeText(Transactions.this, "No resources found", Toast.LENGTH_SHORT).show();



                }

            }

            @Override
            public void onFailure(Call<UserTransactionModel> call, Throwable t) {
                dismissProgressDialog();
                Log.e(TAG, "onResponse:Exp: "+t);

            }
        });

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

    private void dismissProgressDialog() {
        avLoadingIndicatorView.hide();
    }

}
