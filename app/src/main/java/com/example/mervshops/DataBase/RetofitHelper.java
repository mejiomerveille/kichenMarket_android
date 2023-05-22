package com.example.mervshops.DataBase;

import com.example.mervshops.ApiService;

import retrofit2.Retrofit;

public class RetofitHelper{
        public static ApiService instance(){
                 Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("http://192.168.124.79:8000/api/")
                        .build();
            return retrofit.create(ApiService.class);

        }

}