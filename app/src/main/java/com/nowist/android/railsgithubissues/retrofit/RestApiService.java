package com.nowist.android.railsgithubissues.retrofit;

import com.nowist.android.railsgithubissues.models.Comment;
import com.nowist.android.railsgithubissues.models.IssueModel;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Url;

/**
 * Created by mayankverma on 09/07/17.
 */

public interface RestApiService {

    @GET("issues")
    Call<ArrayList<IssueModel>> getIssues();

    @GET
    Call<ArrayList<Comment>> getComments(@Url String commentsUri);

}
