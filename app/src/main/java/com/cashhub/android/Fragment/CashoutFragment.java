package com.cashhub.android.Fragment;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cashhub.android.Activities.WithdrawMoney;
import com.cashhub.android.Adapter.CashoutAdapter;
import com.cashhub.android.Models.PayOutDataModel;
import com.cashhub.android.Models.PayoutArray;
import com.cashhub.android.R;
import com.cashhub.android.Services.sharedConstant;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class CashoutFragment extends Fragment {

    private List<PayOutDataModel.Reward> rewardList;
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

    public CashoutFragment(List<PayOutDataModel.Reward> rewardList) {
        this.rewardList = rewardList;
    }
    /*public CashoutFragment(){

    }*/


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_cashout, container, false);

        loadPayout(view);
       /* cashoutAdapter.setOnItemClickListener(new CashoutAdapter.ClickListener() {
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
*/

        return view;


    }

    private void setRecycler(View view, ArrayList<PayoutArray> payoutArrayArrayList, PayOutDataModel.Reward data, int id) {
        recyclerView = view.findViewById(R.id.recylerViewCashout);
        cashoutAdapter = new CashoutAdapter(payoutArrayArrayList, mActivity);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mActivity);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(cashoutAdapter);
        int coins = Integer.valueOf(sharedConstant.getSharedPreferenceString(mActivity, "Coins", ""));
        int PayoutLimit = Integer.parseInt(data.getRedeemLimit().trim());

        cashoutAdapter.setOnItemClickListener((position, v) -> {
            if (coins < PayoutLimit) {
                if (mActivity != null) {
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
            } else {
                Intent intent = new Intent(mActivity, WithdrawMoney.class);
                intent.putStringArrayListExtra("payoutAmount", data.getPayoutValue());
                intent.putExtra("rewardTitle", data.getTitle());
                intent.putExtra("rewardId", id);
                intent.putExtra("rewardImage", data.getImageUrl());

                startActivity(intent);

            }

            //  Toast.makeText(mActivity, "pay", Toast.LENGTH_SHORT).show();
        });


    }

    private void loadPayout(View view) {
        TextView paytm = view.findViewById(R.id.paytm);
        TextView pubg = view.findViewById(R.id.pubg);
        TextView freeFire = view.findViewById(R.id.freefire);
        TextView paypal = view.findViewById(R.id.paypal);
        payoutArrayArrayList = new ArrayList<>();
        int p = 0;
        for (String data : rewardList.get(1).getPayoutReward()) {
            PayoutArray pay = new PayoutArray();
            pay.setTitle(data);
            pay.setShortTxt(rewardList.get(1).getPayoutValue().get(p).trim() + " Coins");
            pay.setPayImage(rewardList.get(1).getImageUrl());
            payoutArrayArrayList.add(pay);
            p++;
        }
        setRecycler(view, payoutArrayArrayList, rewardList.get(1), 1);
        paytm.setTextColor(mActivity.getColor(R.color.colorPrimaryDark));

        paytm.setOnClickListener(v -> {
            paytm.setTextColor(mActivity.getColor(R.color.colorPrimaryDark));
            pubg.setTextColor(mActivity.getColor(R.color.colorPrimary));
            freeFire.setTextColor(mActivity.getColor(R.color.colorPrimary));
            paypal.setTextColor(mActivity.getColor(R.color.colorPrimary));
            int i = 0;
            payoutArrayArrayList.clear();
            for (String data : rewardList.get(1).getPayoutReward()) {
                PayoutArray pay = new PayoutArray();
                pay.setTitle(data);
                pay.setShortTxt(rewardList.get(1).getPayoutValue().get(i).trim() + " Coins");
                pay.setPayImage(rewardList.get(1).getImageUrl());

                payoutArrayArrayList.add(pay);
                i++;
            }
            setRecycler(view, payoutArrayArrayList, rewardList.get(1), 1);

        });
        pubg.setOnClickListener(v -> {
            pubg.setTextColor(mActivity.getColor(R.color.colorPrimaryDark));
            paytm.setTextColor(mActivity.getColor(R.color.colorPrimary));
            freeFire.setTextColor(mActivity.getColor(R.color.colorPrimary));
            paypal.setTextColor(mActivity.getColor(R.color.colorPrimary));
            int i = 0;
            payoutArrayArrayList.clear();
            for (String data : rewardList.get(2).getPayoutReward()) {
                PayoutArray pay = new PayoutArray();
                pay.setTitle(data);
                pay.setShortTxt(rewardList.get(2).getPayoutValue().get(i).trim() + " Coins");
                pay.setPayImage(rewardList.get(2).getImageUrl());
                payoutArrayArrayList.add(pay);
                i++;
            }
            setRecycler(view, payoutArrayArrayList, rewardList.get(2), 2);

        });
        paypal.setOnClickListener(v -> {
            paypal.setTextColor(mActivity.getColor(R.color.colorPrimaryDark));
            pubg.setTextColor(mActivity.getColor(R.color.colorPrimary));
            freeFire.setTextColor(mActivity.getColor(R.color.colorPrimary));
            paytm.setTextColor(mActivity.getColor(R.color.colorPrimary));

            int i = 0;
            payoutArrayArrayList.clear();
            for (String data : rewardList.get(3).getPayoutReward()) {
                PayoutArray pay = new PayoutArray();
                pay.setTitle(data);
                pay.setShortTxt(rewardList.get(3).getPayoutValue().get(i).trim() + " Coins");
                pay.setPayImage(rewardList.get(3).getImageUrl());
                payoutArrayArrayList.add(pay);
                i++;
            }
            setRecycler(view, payoutArrayArrayList, rewardList.get(3), 4);


        });
        freeFire.setOnClickListener(v -> {
            freeFire.setTextColor(mActivity.getColor(R.color.colorPrimaryDark));
            pubg.setTextColor(mActivity.getColor(R.color.colorPrimary));
            paytm.setTextColor(mActivity.getColor(R.color.colorPrimary));
            paypal.setTextColor(mActivity.getColor(R.color.colorPrimary));

            int i = 0;
            payoutArrayArrayList.clear();
            for (String data : rewardList.get(0).getPayoutReward()) {
                PayoutArray pay = new PayoutArray();
                pay.setTitle(data);
                pay.setShortTxt(rewardList.get(0).getPayoutValue().get(i).trim() + " Coins");
                pay.setPayImage(rewardList.get(0).getImageUrl());

                payoutArrayArrayList.add(pay);
                i++;
            }
            setRecycler(view, payoutArrayArrayList, rewardList.get(0), 3);

        });


    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof FragmentActivity) {
            mActivity = (FragmentActivity) context;
        }
    }
}
