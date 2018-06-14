package com.howard.retrofitdemo;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.howard.retrofitdemo.api.APILink;
import com.howard.retrofitdemo.api.APIService;
import com.howard.retrofitdemo.entity.MovieListingDTO;
import com.howard.retrofitdemo.mapper.DTOModelEntitiesDataMapper;
import com.howard.retrofitdemo.model.Movie;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DemoActivity extends AppCompatActivity {

    RecyclerView recyclerView;

    ProgressBar progressBar;

    SwipeRefreshLayout swipeRefreshLayout;

    protected List<Movie> movieList;
    int page = 1;
    DTOModelEntitiesDataMapper mapper;
    protected MovieRecyclerAdapter adapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.rvMovies);
        progressBar = findViewById(R.id.progressBar);
        swipeRefreshLayout = findViewById(R.id.swipeRefresh);

        if(movieList == null){
            movieList = new ArrayList<>();
            setUpList();
            callApi();
        } else {
            adapter.setData(movieList);
        }
    }

    private void callApi() {
        progressBar.setVisibility(View.VISIBLE);

        APIService.getInstance().getNowPlaying(APILink.API_KEY, page, Locale.getDefault().getLanguage())
                .enqueue(new Callback<MovieListingDTO>() {
                    @Override
                    public void onResponse(Call<MovieListingDTO> call, Response<MovieListingDTO> response) {
                        progressBar.setVisibility(View.GONE);
                        if (response.body() != null) {
                            movieList = mapper.transform(response.body());
                            adapter.setData(movieList);

                        } else
                            Toast.makeText(DemoActivity.this, response.message() != null ? response.message() : "Empty", Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onFailure(Call<MovieListingDTO> call, Throwable t) {

                    }
                });
    }

    private void setUpList() {
        mapper = new DTOModelEntitiesDataMapper();

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);

        adapter = new MovieRecyclerAdapter(DemoActivity.this);
        adapter.setData(movieList==null ? new ArrayList<Movie>(): movieList);
        recyclerView.setAdapter(adapter);
        adapter.setListener(new MovieRecyclerAdapter.IClickListener() {
            @Override
            public void onItemClick(Movie movie) {
                Toast.makeText(DemoActivity.this, movie.getOriginalTitle(), Toast.LENGTH_SHORT).show();
            }
        });

        this.recyclerView.setAdapter(adapter);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                callApi();
            }
        });
    }
}
