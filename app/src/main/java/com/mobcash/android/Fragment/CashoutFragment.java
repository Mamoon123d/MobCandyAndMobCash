package com.mobcash.android.Fragment;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mobcash.android.Activities.WithdrawMoney;
import com.mobcash.android.Models.PayoutArray;
import com.mobcash.android.Services.sharedConstant;
import com.mobcash.android.Adapter.CashoutAdapter;
import com.mobcash.android.R;

import java.util.ArrayList;

import static java.lang.Integer.valueOf;


/**
 * A simple {@link Fragment} subclass.
 */
public class CashoutFragment extends Fragment {

    protected FragmentActivity mActivity;

    String TAG = "testing";
    ArrayList<String> SelectAmount;
    ArrayList<PayoutArray> payoutArrayArrayList;
    RecyclerView recyclerView;
    CashoutAdapter cashoutAdapter;

    public CashoutFragment() {

    }

    public CashoutFragment(ArrayList<String> payoutValuesList, ArrayList<PayoutArray> payoutArrayList) {
        SelectAmount = payoutValuesList;
        payoutArrayArrayList = payoutArrayList;

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_cashout, container, false);

        recyclerView = view.findViewById(R.id.recylerViewCashout);
        cashoutAdapter = new CashoutAdapter(payoutArrayArrayList, mActivity);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mActivity);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(cashoutAdapter);

        cashoutAdapter.setOnItemClickListener(new CashoutAdapter.ClickListener() {
                           @Override
                           public void onItemClick(int position, View v) {

                               int stringcoins = Integer.valueOf(sharedConstant.getSharedPreferenceString(mActivity, "Coins", ""));
                               int PayoutLimit = sharedConstant.getSharedPreferenceInt(mActivity, "PaycoinLimit", 0);
                               if (stringcoins < PayoutLimit) {

                                   if (mActivity!= null) {
                                       AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
                                       builder.setTitle("Message");
                                       builder.setMessage("Minimum Transfer Coins Limit is 5000.");
                                       builder.setPositiveButton("Ok",
                                               new DialogInterface.OnClickListener() {
                                                   public void onClick(DialogInterface dialog,
                                                                       int which) {
                                                       dialog.dismiss();
                                                   }
                                               });
                                       builder.setCancelable(false);
                                       builder.show();

                   }
                }
                else {
                        Intent intent = new Intent(mActivity, WithdrawMoney.class);
                        intent.putStringArrayListExtra("payoutAmount", SelectAmount);
                        startActivity(intent);

                    }

            }
        });


        return view;


    }
    @Override
    public void onAttach(@NonNull Context context ) {
        super.onAttach(context);
        if (context instanceof FragmentActivity){
            mActivity = (FragmentActivity) context;
        }
    }
}
