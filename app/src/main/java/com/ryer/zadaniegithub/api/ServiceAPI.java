package com.ryer.zadaniegithub.api;


import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ServiceAPI {
    @GET("repos/{owner}/{repository}/commits")
    Call<List<Repo>> getRepo(@Path("owner") String owner,@Path("repository") String repository);
}
