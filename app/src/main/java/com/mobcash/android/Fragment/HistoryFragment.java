package com.mobcash.android.Fragment;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mobcash.android.Adapter.HistoryAdapter;
import com.mobcash.android.Models.PayoutHistory;
import com.mobcash.android.R;

import java.util.ArrayList;

public class HistoryFragment extends Fragment {
    private FragmentActivity mActivity;
    private ArrayList<PayoutHistory> payoutHistories;
    String TAG = "testing";
    private boolean checkPayoutArrayListNotEmpty;

    public HistoryFragment() {
        // Required empty public constructor
    }


    public HistoryFragment(ArrayList<PayoutHistory> payoutHistoryArrayList, boolean arrayflg) {
        payoutHistories = payoutHistoryArrayList;
        checkPayoutArrayListNotEmpty = arrayflg;

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_history, container, false);
        TextView txt_msg = (TextView) view.findViewById(R.id.txt_msg);

        RecyclerView recyclerView = view.findViewById(R.id.recylerViewHistory);


        if (checkPayoutArrayListNotEmpty) {
            txt_msg.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            HistoryAdapter historyAdapter = new HistoryAdapter(payoutHistories, mActivity);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mActivity);
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(historyAdapter);

        } else {
            txt_msg.setVisibility(View.VISIBLE);
            txt_msg.setText("You don't have any history logs.");
            recyclerView.setVisibility(View.GONE);
        }
        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof FragmentActivity) {
            mActivity = (FragmentActivity) context;
        }
    }

}
