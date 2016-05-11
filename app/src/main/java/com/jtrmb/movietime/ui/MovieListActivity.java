package com.jtrmb.movietime.ui;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

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
    
    /* Member variables used to implement endless scroll */
    private int previousTotal = 0;
    private boolean isLoading = true;
    private int visibleThreshold = 5;
    int firstVisibleItem, visibleItemCount, totalItemCount;
    int currentPage = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);
        ButterKnife.bind(this);

        /* Gets the user's search query */
        Intent intent = getIntent();
        final String searchQuery = intent.getStringExtra("searchQuery");

        /* Shows loading progress dialog spinner */
        initializeProgressDialog();
        mLoadingProgressDialog.show();

        /* Retrieves search results asynchronously so API call does not block UI thread */
        TaskParams params = new TaskParams(searchQuery, currentPage);
        new GetMovieTask().execute(params);

        mRecyclerView.setLayoutManager(mManager);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            /* Triggers a new GetMovieTask when the user scrolls to the end of the list */
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

                /* Checks to see if the end of the list is close and, if so, gets the next page of movies */
                if (!isLoading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
                    currentPage++;
                    TaskParams params = new TaskParams(searchQuery, currentPage);
                    new GetMovieTask().execute(params);
                    isLoading = true;
                }
            }
        });
    }

    /* Retrieves movie search results asynchronously and adds them to the RecyclerView adapter */
    private class GetMovieTask extends AsyncTask<TaskParams, Void, List<MovieDb>> {
        @Override
        protected List<MovieDb> doInBackground(TaskParams... params) {
            /* Uses both the search query and the page number to retrieve the correct results */
            return SearchService.movieSearch(params[0].query, 0, params[0].page, getApplicationContext());
        }

        @Override
        protected void onPostExecute(List<MovieDb> result) {
            if (mAdapter != null) {
                /* Adds the new results to the existing adapter, since this is not the first time retrieving movies */
                mAdapter.addMovies(result);
            } else {
                /* Creates a new adapter with the results, since this is the first time retrieving movies */
                mLoadingProgressDialog.dismiss();
                if (result.size() == 0) {
                    showFailureDialog("Please try another search.");
                }
                mAdapter = new MovieListAdapter(getApplicationContext(), result);
                mRecyclerView.setAdapter(mAdapter);
            }
            mRecyclerView.setHasFixedSize(true);
        }
    }

    /* Builds a TaskParams object to allow for passing both the search query and the page number to GetMovieTask */
    private static class TaskParams {
        String query;
        int page;

        TaskParams(String query, int page) {
            this.query = query;
            this.page = page;
        }
    }

    /* Builds a loading progress dialog spinner so that the user knows movie information is being retrieved */
    private void initializeProgressDialog() {
        mLoadingProgressDialog = new ProgressDialog(this);
        mLoadingProgressDialog.setTitle(getString(R.string.searching));
        mLoadingProgressDialog.setMessage(getString(R.string.searching_for_movies));
        mLoadingProgressDialog.setCancelable(false);
    }

    /* Shows an alert dialog if the movie search finds no results */
    private void showFailureDialog(String message) {
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.no_results))
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        /* Transition to the MainActivity so the user can search again */
                        Intent intent = new Intent(MovieListActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
}
