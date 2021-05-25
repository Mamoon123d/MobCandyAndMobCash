package com.mobcash.android.Services;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

private static Retrofit retrofit;
    private static Retrofit retrofit2;



    //Define the base URL//

private static final String BASE_URL = "https://mobcash.app/api/v1/";
    private static final String BASE_URL2 = "https://couponhub.app/api/v1/";


//Create the Retrofit instance//

    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)

//Add the converter//
                    .addConverterFactory(GsonConverterFactory.create())

//Build the Retrofit instance//
                    .build();
        }
        return retrofit;
    }

    public static Retrofit getRetrofitInstance2() {
        if (retrofit2 == null) {
            retrofit2 = new Retrofit.Builder()
                    .baseUrl(BASE_URL2)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit2;
    }


}
