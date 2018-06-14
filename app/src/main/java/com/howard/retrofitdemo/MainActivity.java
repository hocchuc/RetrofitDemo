package com.howard.retrofitdemo;

import android.os.Bundle;
import android.util.Log;
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

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;

    ProgressBar progressBar;

    SwipeRefreshLayout swipeRefreshLayout;

    protected MovieRecyclerAdapter adapter;
    protected List<Movie> movieList;
    protected DTOModelEntitiesDataMapper mapper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.rvMovies);
        progressBar = findViewById(R.id.progressBar);
        swipeRefreshLayout = findViewById(R.id.swipeRefresh);

        if(movieList == null) {
            setUpListView();
            callAPI();
        } else {
            //there is already data? screen must be rotating or tab switching
           adapter.setData(movieList);
        }
    }

    private void callAPI() {
        progressBar.setVisibility(View.VISIBLE);

        APIService.getInstance().getNowPlaying(APILink.API_KEY, 1, Locale.getDefault().getLanguage()).enqueue(
                new Callback<MovieListingDTO>() {
                    @Override
                    public void onResponse(Call<MovieListingDTO> call, Response<MovieListingDTO> response) {
                        progressBar.setVisibility(View.GONE);
                        if (response.body() != null) {
                            movieList = mapper.transform(response.body());
                            adapter.setData(movieList);

                        } else
                            Toast.makeText(MainActivity.this, response.message() != null ? response.message() : "Empty", Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onFailure(Call<MovieListingDTO> call, Throwable t) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    }
                }
        );

    }

    private void setUpListView() {
        mapper = new DTOModelEntitiesDataMapper();
        movieList = new ArrayList<>();
        //recyclerView.setHasFixedSize(true);
        // use a linear layout manager
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);

        adapter = new MovieRecyclerAdapter(MainActivity.this);
        adapter.setData(movieList);
        adapter.setListener(new MovieRecyclerAdapter.IClickListener() {
            @Override
            public void onItemClick(Movie movie) {
                Toast.makeText(MainActivity.this, movie.getOriginalTitle(), Toast.LENGTH_SHORT).show();
            }
        });

        this.recyclerView.setAdapter(adapter);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                callAPI();
            }
        });
    }
}
