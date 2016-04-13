package com.jtrmb.movietime.ui;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.widget.Toast;

import com.jtrmb.movietime.R;
import com.jtrmb.movietime.adapters.MovieListAdapter;
import com.jtrmb.movietime.services.SearchService;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import info.movito.themoviedbapi.model.MovieDb;

public class MovieListActivity extends AppCompatActivity {
    @Bind(R.id.recyclerView) RecyclerView mRecyclerView;

    private MovieListAdapter mAdapter;
    private LinearLayoutManager mManager = new LinearLayoutManager(MovieListActivity.this);
    private ProgressDialog mLoadingProgressDialog;

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
        initializeProgressDialog();
        mLoadingProgressDialog.show();

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
                    //need to get more movies
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
            return SearchService.movieSearch(params[0].query, 0, params[0].page, getApplicationContext());
        }

        @Override
        protected void onPostExecute(List<MovieDb> result) {

            if (mAdapter != null) {
                //not the first time getting movies, so add to the existing adapter
                mAdapter.addMovies(result);
            } else {
                //first time getting movies
                mLoadingProgressDialog.dismiss();
                if (result.size() == 0) {
                    showFailureDialog("Please try another search.");
                }
                mAdapter = new MovieListAdapter(getApplicationContext(), result);
                mRecyclerView.setAdapter(mAdapter);
            }
            mRecyclerView.setHasFixedSize(true);
            mRecyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), R.drawable.divider_shape));
        }
    }

    //wrapper class for giving both query and page to GetMovieTask
    private static class TaskParams {
        String query;
        int page;

        TaskParams(String query, int page) {
            this.query = query;
            this.page = page;
        }
    }

    private void initializeProgressDialog() {
        mLoadingProgressDialog = new ProgressDialog(this);
        mLoadingProgressDialog.setTitle(getString(R.string.searching));
        mLoadingProgressDialog.setMessage(getString(R.string.searching_for_movies));
        mLoadingProgressDialog.setCancelable(false);
    }

    private void showFailureDialog(String message) {
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.no_results))
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(MovieListActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
}