package com.mobcash.android.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.mobcash.android.Models.AppOpenModel;
import com.mobcash.android.Models.UserProfileModel;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.mobcash.android.Interface.GetApiData;
import com.mobcash.android.R;
import com.mobcash.android.Services.RetrofitClient;
import com.mobcash.android.Services.sharedConstant;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class UserProfileActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    FirebaseAuth mAuth;
    CircularImageView circleImageView;
    TextView EmailId,fullName,genderAlertTv;
    EditText Number, dob,ReferralCodeED,address;
    List<Address> addresses;
    RadioButton radioButtonMale, radioButtonFemale;
    RadioGroup radioGroup;
    Button SaveButton;
    Spinner spinner;
    Toolbar toolbarWidget;

    DatePickerDialog picker;
    private String gender, actionTypeGet = "get", actionTypePost = "post", occupation, mobileNumber, dateofbirth, userName, userEmail,gen,locationUser;
    String locationAdd;
    //Boolean variable to mark if the transaction is safe
    private boolean isTransactionSafe;
    //Boolean variable to mark if there is any transaction pending
    private boolean isTransactionPending;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        mAuth = FirebaseAuth.getInstance();
        toolbarWidget = findViewById(R.id.toolbar);
        dob = findViewById(R.id.dob);
        radioGroup = findViewById(R.id.gender);
        radioButtonMale = findViewById(R.id.male);
        radioButtonFemale = findViewById(R.id.female);
        SaveButton = findViewById(R.id.SaveButton);
        spinner = findViewById(R.id.spinner);
        fullName = findViewById(R.id.fullName);
        EmailId = findViewById(R.id.email);
        address = findViewById(R.id.location);
        genderAlertTv = findViewById(R.id.genderAlert);
        Number = findViewById(R.id.mobileNumber);
        circleImageView = findViewById(R.id.profileImage);
        ReferralCodeED = findViewById(R.id.referCode);
        spinner.setOnItemSelectedListener(this);
        SaveButton.setOnClickListener(this);

        toolbarWidget.setTitle("");
        setSupportActionBar(toolbarWidget);
        toolbarWidget.setNavigationIcon(R.drawable.ic_back);
        toolbarWidget.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        fullName.setText(mAuth.getCurrentUser().getDisplayName());
        EmailId.setText(mAuth.getCurrentUser().getEmail());
        address.setEnabled(false);

        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
        categories.add("--Select Occupation--");
        categories.add("Student");
        categories.add("Employed");
        categories.add("Unemloyed");
        categories.add("Housewife");
        categories.add("Retired");
        categories.add("Self Employed");


        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(UserProfileActivity.this, android.R.layout.simple_list_item_1, categories);

        dataAdapter.setDropDownViewResource(R.layout.simple_dropdown_item_1line);
        spinner.setAdapter(dataAdapter);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {

                    case R.id.female:
                        radioButtonMale.setChecked(false);
                        gender = "Female";
                        genderAlertTv.setVisibility(View.GONE);
                        break;
                    case R.id.male:
                        radioButtonFemale.setChecked(false);
                        gender = "Male";
                        genderAlertTv.setVisibility(View.GONE);
                        break;
                    default:
                        break;
                }
            }
        });

        dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                dob.setError(null);

                // date picker dialog
                picker = new DatePickerDialog(UserProfileActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                                dateofbirth = String.valueOf(dayOfMonth) + "/" + String.valueOf(monthOfYear + 1) + "/" + String.valueOf(year);
                                Log.e(TAG, "onDateSet: " + dateofbirth);
                                sharedConstant.setSharedPreferenceString(UserProfileActivity.this, "date", dateofbirth);
                                dob.setText(dateofbirth);
                                String d = dob.getText().toString();
                                Log.e(TAG, "onDateSet: " + d);

                            }
                        }, year, month, day);
                picker.getDatePicker().setMaxDate(System.currentTimeMillis());
                picker.show();
            }
        });

        dob.setText(sharedConstant.getSharedPreferenceString(UserProfileActivity.this, "date", ""));

        Picasso.get().load(mAuth.getCurrentUser().getPhotoUrl()).into(circleImageView);

        ReferralCodeED.setText(sharedConstant.getSharedPreferenceString(UserProfileActivity.this, "utm_medium", ""));
        getUserDataDetails();
        
        
        
    }

    private void getUserLocation() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && UserProfileActivity.this.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1000);

        } else {
            LocationManager locationManager = (LocationManager) UserProfileActivity.this.getSystemService(Context.LOCATION_SERVICE);
            assert locationManager != null;
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            try {
                assert location != null;
                String city = currentLocation(location.getLatitude(), location.getLongitude());
                address.setText(city + "," + addresses.get(0).getCountryName());
                locationAdd = address.getText().toString();
                Log.e(TAG, "getLocation: LocationAdd "+locationAdd );
                sendUserDataDetails(mobileNumber, dateofbirth, gen, userEmail, occupation, userName);
            } catch (Exception e) {
                e.printStackTrace();
                locationAdd = address.getText().toString();
                if(address.getText().toString().isEmpty()){
                    String msg = "Your location should not be empty enter your location manually.";
                    address.setEnabled(true);
                    address.requestFocus();
                    showRewardDialog(msg);
                }else{
                    sendUserDataDetails(mobileNumber, dateofbirth, gen, userEmail, occupation, userName);
                }

            }



        }

    }

    private void appOpen() {

        PackageInfo pInfo = null;
        try {
            pInfo = UserProfileActivity.this.getPackageManager().getPackageInfo(UserProfileActivity.this.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        String versionName = pInfo.versionName;
        int versionCode = pInfo.versionCode;

        GetApiData getApiData = RetrofitClient.getRetrofitInstance().create(GetApiData.class);


        Call<AppOpenModel> call = getApiData.getAppOpenDetails(sharedConstant.getSharedPreferenceInt(UserProfileActivity.this, "userId", 0),
                sharedConstant.getSharedPreferenceString(UserProfileActivity.this, "securitytoken", ""), versionName, versionCode);
        call.enqueue(new Callback<AppOpenModel>() {
            @Override
            public void onResponse(Call<AppOpenModel> call, Response<AppOpenModel> response) {
                if (response != null) {
                    if (response.isSuccessful()) {
                        assert response.body() != null;
                        if (response.body().getStatus() == 200) {
                            String amount = String.valueOf(response.body().getUserAmount());
                            String coins = String.valueOf(response.body().getUserCoin());
                            String currency = String.valueOf(response.body().getCurrency());

                            sharedConstant.setSharedPreferenceString(UserProfileActivity.this, "Coins", coins);
                            sharedConstant.setSharedPreferenceString(UserProfileActivity.this, "Amount", amount);
                            sharedConstant.setSharedPreferenceString(UserProfileActivity.this, "Currency", currency);
                            sharedConstant.setSharedPreferenceInt(UserProfileActivity.this, "PaycoinLimit", response.body().getPaycoinLimit());
                            sharedConstant.setSharedPreferenceInt(UserProfileActivity.this, "AttendenceId", response.body().getAttendenceId());
                            sharedConstant.setSharedPreferenceInt(UserProfileActivity.this, "WatchVideoId", response.body().getWatchVideoId());
                            sharedConstant.setSharedPreferenceBoolean(UserProfileActivity.this, "Attendence", response.body().getAttendence());
                            sharedConstant.setSharedPreferenceString(UserProfileActivity.this, "WaitTime", response.body().getWaitTime());
                            sharedConstant.setSharedPreferenceString(UserProfileActivity.this, "AttendenceCoin", String.valueOf(response.body().getAttendenceCoin()));


                            Intent intent = new Intent(UserProfileActivity.this, MainActivity.class);
                            startActivity(intent);
                            UserProfileActivity.this.finish();


                        } else {
                            Toast.makeText(UserProfileActivity.this, "System Message: " + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(UserProfileActivity.this, "System Message: " + response.errorBody(), Toast.LENGTH_SHORT).show();
                    }
                } else
                    Toast.makeText(UserProfileActivity.this, "System Message: " + response.errorBody(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<AppOpenModel> call, Throwable t) {

                Log.e(TAG, "onFailure: " + t);
//                Toast.makeText(UserProfileActivity.this, getString(R.string.systemmessage) + t, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getUserDataDetails() {
        GetApiData getApiData = RetrofitClient.getRetrofitInstance().create(GetApiData.class);
        Call<UserProfileModel> call = getApiData.getUserProfileData(sharedConstant.getSharedPreferenceInt(UserProfileActivity.this, "userId", 0)
                , sharedConstant.getSharedPreferenceString(UserProfileActivity.this, "securitytoken", "")
                , sharedConstant.getSharedPreferenceString(UserProfileActivity.this, "versionName", ""),
                sharedConstant.getSharedPreferenceInt(UserProfileActivity.this, "versionCode", 0), actionTypeGet);

        call.enqueue(new Callback<UserProfileModel>() {
            @Override
            public void onResponse(Call<UserProfileModel> call, Response<UserProfileModel> response) {

                if (response != null) {
                    if (response.isSuccessful()) {
                        assert response.body() != null;
                        if (response.body().getStatus() == 200) {
                            fullName.setText(response.body().getUserName());
                            userName = fullName.getText().toString();

                            EmailId.setText(response.body().getUserEmail());
                            userEmail = EmailId.getText().toString();
                            Number.setText(response.body().getMobileNumber());
                            address.setText(response.body().getLocation());
                            String gen = response.body().getGender();
                            Log.e(TAG, "onResponse:Gen "+gen );
                            if (gen != null) {
                                if (gen.equals("Male")) {
                                    radioButtonMale.setChecked(true);
                                } else {
                                    assert response.body().getGender() != null;
                                    if (response.body().getGender().equals("")) {

                                    } else {
                                        radioButtonFemale.setChecked(true);
                                    }
                                }
                            }


                            String myString = response.body().getOccupation(); //the value you want the position for

                            ArrayAdapter dataAdapter = (ArrayAdapter) spinner.getAdapter(); //cast to an ArrayAdapter

                            int spinnerPosition = dataAdapter.getPosition(myString);


                            spinner.setSelection(spinnerPosition);
                            Log.e(TAG, "onResponse:spin " + spinnerPosition);


                        } else {
                            Toast.makeText(UserProfileActivity.this, "System Message: " + response.errorBody(), Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    Toast.makeText(UserProfileActivity.this, "System Message: " + response.errorBody(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserProfileModel> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t);
            }
        });

    }

    private void sendUserDataDetails(String mobileNumber, String dateob, String gen, String emailaddress, String occupation, String userName) {

        PackageInfo pInfo = null;
        try {
            pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        String versionName = pInfo.versionName;
        int versionCode = pInfo.versionCode;

        GetApiData getApiData = RetrofitClient.getRetrofitInstance().create(GetApiData.class);
        Call<UserProfileModel> call = getApiData.sendUserProfileData(sharedConstant.getSharedPreferenceInt(UserProfileActivity.this, "userId", 0)
                , sharedConstant.getSharedPreferenceString(UserProfileActivity.this, "securitytoken", "")
                , versionName, versionCode, actionTypePost, userName, emailaddress, mobileNumber, gen, locationAdd, occupation, dateob);

        call.enqueue(new Callback<UserProfileModel>() {
            @Override
            public void onResponse(Call<UserProfileModel> call, Response<UserProfileModel> response) {
                if (response != null) {
                    if (response.isSuccessful()) {
                        assert response.body() != null;
                        if (response.body().getStatus() == 200) {
                            appOpen();
                            Toast.makeText(UserProfileActivity.this, "You have successfully submitted your profile details.Your coins will be submitted to you soon", Toast.LENGTH_SHORT).show();
                            sharedConstant.setSharedPreferenceInt(UserProfileActivity.this,"flag",1);
                            sharedConstant.setSharedPreferenceInt(UserProfileActivity.this,"flagForSave",1);


                        } else {
                            Toast.makeText(UserProfileActivity.this, "System Message: " + response.errorBody(), Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    Toast.makeText(UserProfileActivity.this, "System Message: " + response.errorBody(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserProfileModel> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t);
            }
        });

    }


    private void invalidateData() {
        userEmail = EmailId.getText().toString();
        mobileNumber = Number.getText().toString();
        userName = fullName.getText().toString();
        gen = gender;
        dateofbirth = dob.getText().toString();
        occupation = spinner.getSelectedItem().toString();

        if (userEmail.isEmpty()) {
            EmailId.setError("User Email Required");
            EmailId.requestFocus();
        }
        else if (mobileNumber.isEmpty()) {
            Number.setError("Paytm Number Required");
            Number.requestFocus();
        }
        else if (dateofbirth.isEmpty()) {
            dob.setError("Date Of Birth Required");
            dob.requestFocus();
        }

        else if (!radioButtonFemale.isChecked() && !radioButtonMale.isChecked()) {
            genderAlertTv.setVisibility(View.VISIBLE);
        }
        else if (occupation == "--Select Occupation--") {
            TextView errorText = (TextView) spinner.getSelectedView();
            errorText.setError("");
            errorText.setTextColor(Color.RED);//just to highlight that this is an error
            errorText.setText("Please Select Occupation");
//            spinner.setError("Mobile required");
            spinner.requestFocus();
        }
        else {
            getUserLocation();
        }


    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1000) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                LocationManager locationManager = (LocationManager) UserProfileActivity.this.getSystemService(Context.LOCATION_SERVICE);
                assert locationManager != null;
                @SuppressLint("MissingPermission") Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                try {
                    assert location != null;
                    String city = currentLocation(location.getLatitude(), location.getLongitude());
                    address.setText(city + "," + addresses.get(0).getCountryName());
                    locationAdd = address.getText().toString();
                } catch (Exception e) {
                    e.printStackTrace();
                    String msg = "Your location is not found please enter your location manually.";
                        address.setEnabled(true);
                        address.requestFocus();
                        showRewardDialog(msg);
//                    Toast.makeText(UserProfileActivity.this, "Not Found!!", Toast.LENGTH_SHORT).show();
                }
            } else if (Build.VERSION.SDK_INT >= 23 && !shouldShowRequestPermissionRationale(permissions[0])) {
                String msg ="These permissions are mandatory to get your location. You need to allow them.";
                showRewardDialog(msg);
                Intent i = new Intent();
                i.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                i.addCategory(Intent.CATEGORY_DEFAULT);
                i.setData(Uri.parse("package:" + UserProfileActivity.this.getPackageName()));
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                UserProfileActivity.this.startActivity(i);                    // User selected the Never Ask Again Option
            } else {
                String msg ="These permissions are mandatory to get your location. You need to allow them.";
                showRewardDialog(msg);
            }
        }


    }

    private void showRewardDialog(String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(UserProfileActivity.this);
        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        builder.setMessage(msg);
        builder.setCancelable(false);
        AlertDialog dialog = builder.create();
        if(!UserProfileActivity.this.isFinishing())
        {
            dialog.show();
        }


    }


    private String currentLocation(double lat, double lon) {
        String currentCity = "";
        Log.e(TAG, "currentLocation:city " + lat);
        Log.e(TAG, "currentLocation:city " + lon);


        Geocoder geocoder = new Geocoder(UserProfileActivity.this, Locale.getDefault());
        try {
            addresses = geocoder.getFromLocation(lat, lon, 10);
            if (addresses.size() > 0) {

                for (Address adr : addresses) {
                    if (adr.getLocality() != null && adr.getLocality().length() > 0) {
                        currentCity = adr.getLocality();
                        Log.e(TAG, "currentLocation:city " + currentCity);

                    }

                }
            }


        } catch (IOException e) {
            e.printStackTrace();
        }


        return currentCity;
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.SaveButton) {
            if (sharedConstant.getSharedPreferenceInt(UserProfileActivity.this, "flagForSave", 0) != 0) {
                Toast.makeText(UserProfileActivity.this, "You have already saved your profile details !!!", Toast.LENGTH_SHORT).show();
            } else {
                invalidateData();
            }
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        occupation = spinner.getSelectedItem().toString();


    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    


    private void dismissProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }


    @Override
    public void onDestroy() {
        dismissProgressDialog();
        super.onDestroy();
    }




    @Override
    public void onPause() {
        super.onPause();
        dismissProgressDialog();
    }
}