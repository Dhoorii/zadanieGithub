package com.ryer.zadaniegithub.api;

import android.content.Context;
import android.content.SharedPreferences;
import android.webkit.DateSorter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ryer.zadaniegithub.MainActivity;
import com.ryer.zadaniegithub.RecyclerViewAdapter;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Client implements Callback<List<Repo>> {
    public static final String URL = "https://api.github.com/";
    public static Retrofit retrofit = null;
    private String owner,repository;
    private List<Repo> repoList;
    SharedPreferences settings;
    Context context;
    public static Retrofit getClient(){
        if (retrofit == null)
        {
            retrofit = new Retrofit.Builder()
                    .baseUrl(URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
    public void start(String owner,String repository,Context context)
    {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        getClient();
        ServiceAPI serviceAPI = retrofit.create(ServiceAPI.class);
        this.context = context;
        this.repository = repository;
        this.owner = owner;
        Call<List<Repo>> call = serviceAPI.getRepo(owner,repository);
        call.enqueue(this);
    }
    @Override
    public void onResponse(Call<List<Repo>> call, Response<List<Repo>> response) {
        if (response.isSuccessful())
        {
            List<Repo> repoList = response.body();
            Collections.sort(repoList, new Comparator<Repo>() {
                @Override
                public int compare(Repo r1, Repo r2) {
                    Date x1 =  r1.getCommit().getAuthor().getDate();
                    Date x2 =  r2.getCommit().getAuthor().getDate();

                    return  x1.compareTo(x2);
                }
            });
            System.out.println("dziala");
            MainActivity.adapter = new RecyclerViewAdapter(context,repoList);
            MainActivity.recyclerView.setAdapter(MainActivity.adapter);

            settings = context.getSharedPreferences(owner+" "+repository, 0);
            SharedPreferences.Editor editor = settings.edit();

            Gson gson = new Gson();
            String json = gson.toJson(repoList);
            MainActivity.repoList = repoList;
            editor.putString("MyObject", json);
            editor.commit();

        }
    }

    @Override
    public void onFailure(Call<List<Repo>> call, Throwable t) {
        t.printStackTrace();
        Gson gson = new Gson();
        settings = context.getSharedPreferences(owner+" "+repository, 0);
        String json = settings.getString("MyObject", "");
        Repo[] obj = gson.fromJson(json, Repo[].class);


        List<Repo> repos = new ArrayList<>(Arrays.asList(obj));
        Collections.sort(repos, new Comparator<Repo>() {
            @Override
            public int compare(Repo r1, Repo r2) {
                Date x1 =  r1.getCommit().getAuthor().getDate();
                Date x2 =  r2.getCommit().getAuthor().getDate();

                return  x1.compareTo(x2);
            }
        });
        MainActivity.adapter = new RecyclerViewAdapter(context,repos);
        MainActivity.recyclerView.setAdapter(MainActivity.adapter);
        MainActivity.repoList = repos;
    }
}
