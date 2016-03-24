package com.epicodus.movietime.ui;

import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.epicodus.movietime.R;
import com.epicodus.movietime.adapters.MovieListAdapter;
import com.epicodus.movietime.services.SearchService;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import info.movito.themoviedbapi.model.MovieDb;

public class MovieListActivity extends AppCompatActivity {
    @Bind(R.id.recyclerView) RecyclerView mRecyclerView;

    private MovieListAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        String searchQuery = intent.getStringExtra("searchQuery");

        new GetMovieTask().execute(searchQuery);
    }

    private class GetMovieTask extends AsyncTask<String, Void, List<MovieDb>> {
        @Override
        protected List<MovieDb> doInBackground(String... params) {
            List<MovieDb> movies = SearchService.movieSearch(params[0], 0, 1);
            return movies;
        }

        @Override
        protected void onPostExecute(List<MovieDb> result) {
            mAdapter = new MovieListAdapter(getApplicationContext(), result);

            mRecyclerView.setAdapter(mAdapter);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MovieListActivity.this);
            mRecyclerView.setLayoutManager(layoutManager);
            mRecyclerView.setHasFixedSize(true);
            mRecyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), R.drawable.divider_shape));
        }
    }
}
