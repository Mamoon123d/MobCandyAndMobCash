package com.mobcash.android.Fragment;


import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.mobcash.android.Activities.GoogleLogin;
import com.mobcash.android.Activities.InviteFriends;
import com.mobcash.android.BuildConfig;
import com.mobcash.android.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingFragment extends Fragment implements View.OnClickListener {
    protected FragmentActivity mActivity;
    private LinearLayout privacypolicy, termcondition ,HowsItWork,Help,InviteAndEarn,Transactions;
    TextView version;

    public SettingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_setting, container, false);

        privacypolicy = view.findViewById(R.id.txt_privacy);
        termcondition = view.findViewById(R.id.txt_termcondition);
        HowsItWork = view.findViewById(R.id.howsItWork);
        Help = view.findViewById(R.id.helpSupport);
        InviteAndEarn = view.findViewById(R.id.inviteAndEarn);
        version = view.findViewById(R.id.versionCode);
        Transactions = view.findViewById(R.id.txt_transaction);

        final String versionName = BuildConfig.VERSION_NAME;

        version.setText("Version "+versionName);
        privacypolicy.setOnClickListener(this);
        termcondition.setOnClickListener(this);
        HowsItWork.setOnClickListener(this);
        Help.setOnClickListener(this);
        InviteAndEarn.setOnClickListener(this);
        Transactions.setOnClickListener(this);

        return view;
    }


    private void webViewLoad(String url, String title) {
        AlertDialog.Builder alert = new AlertDialog.Builder(mActivity);
        alert.setTitle(title);

        WebView wv = new WebView(mActivity);
        wv.loadUrl(url);
        wv.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

        alert.setView(wv);
        alert.setNegativeButton("Accept", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        alert.show();
    }

    public void onClick(View v) {
        int i = v.getId();
        switch (i) {
            case R.id.txt_privacy:
                inAppOpenBrowser("https://mobcash.app/privacypolicy.html");
                break;

            case R.id.txt_termcondition:
                inAppOpenBrowser("https://mobcash.app/termncondition.html");
                break;

            case R.id.helpSupport:
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto", "apphelpmg@gmail.com", null));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "MobCash : Help/Support");
                mActivity.startActivity(Intent.createChooser(emailIntent, null));
                break;

            case R.id.howsItWork:
                inAppOpenBrowser("https://mobcash.app/howitwork.html");
                break;

            case R.id.inviteAndEarn:
                Intent myIntent = new Intent(mActivity, InviteFriends.class);
                startActivity(myIntent);
                break;

            case R.id.txt_transaction:
                Intent myIntent1 = new Intent(mActivity, com.mobcash.android.Activities.Transactions.class);
                startActivity(myIntent1);
                break;

        }
    }

    @Override
    public void onAttach(@NonNull Context context ) {
        super.onAttach(context);
        if (context instanceof FragmentActivity){
            mActivity = (FragmentActivity) context;
        }
    }

    private void inAppOpenBrowser(String url) {
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        builder.setToolbarColor(ContextCompat.getColor(requireActivity(), R.color.colorPrimaryDark));
        builder.addDefaultShareMenuItem();

        CustomTabsIntent anotherCustomTab = new CustomTabsIntent.Builder().build();


        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher_background);
        int requestCode = 100;
        Intent intent = anotherCustomTab.intent;
        intent.setData(Uri.parse(url));

        PendingIntent pendingIntent = PendingIntent.getActivity(requireActivity(),
                requestCode,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setActionButton(bitmap, "Android", pendingIntent, true);
        builder.setShowTitle(true);


        CustomTabsIntent customTabsIntent = builder.build();
        customTabsIntent.launchUrl(requireActivity(), Uri.parse(url));

    }


}
