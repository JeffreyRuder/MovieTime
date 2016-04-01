package com.epicodus.movietime.ui;

import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import com.epicodus.movietime.R;
import com.epicodus.movietime.adapters.MovieListAdapter;
import com.epicodus.movietime.services.SearchService;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import info.movito.themoviedbapi.TmdbMovies;
import info.movito.themoviedbapi.model.MovieDb;
import info.movito.themoviedbapi.model.config.TmdbConfiguration;

public class MovieListActivity extends AppCompatActivity {
    @Bind(R.id.recyclerView) RecyclerView mRecyclerView;

    private MovieListAdapter mAdapter;
    private LinearLayoutManager mManager = new LinearLayoutManager(MovieListActivity.this);

    private int previousTotal = 0;
    private boolean isLoading = true;
    private int visibleThreshold = 5;
    int firstVisibleItem, visibleItemCount, totalItemCount;
    int pageCounter = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        final String searchQuery = intent.getStringExtra("searchQuery");
        TaskParams params = new TaskParams(searchQuery, pageCounter);

        new GetMovieTask().execute(params);
        mRecyclerView.setLayoutManager(mManager);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                visibleItemCount = mRecyclerView.getChildCount();
                totalItemCount = mManager.getItemCount();
                firstVisibleItem = mManager.findFirstVisibleItemPosition();

                if (isLoading) {
                    if (totalItemCount > previousTotal) {
                        isLoading = false;
                        previousTotal = totalItemCount;
                    }
                }
                if (!isLoading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
                    pageCounter ++;
                    TaskParams params = new TaskParams(searchQuery, pageCounter);
                    new GetMovieTask().execute(params);
                    isLoading = true;
                }
            }
        });

    }

    private class GetMovieTask extends AsyncTask<TaskParams, Void, List<MovieDb>> {
        @Override
        protected List<MovieDb> doInBackground(TaskParams... params) {
            List<MovieDb> movies = SearchService.movieSearch(params[0].query, 0, params[0].page);
            return movies;
        }

        @Override
        protected void onPostExecute(List<MovieDb> result) {
            if (mAdapter != null) {
                //not the first time getting movies
                mAdapter.addMovies(result);
            } else {
                //first time getting movies
                mAdapter = new MovieListAdapter(getApplicationContext(), result);
                mRecyclerView.setAdapter(mAdapter);
            }
            mRecyclerView.setHasFixedSize(true);
            mRecyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), R.drawable.divider_shape));
        }
    }

    private static class TaskParams {
        String query;
        int page;

        TaskParams(String query, int page) {
            this.query = query;
            this.page = page;
        }
    }
}
