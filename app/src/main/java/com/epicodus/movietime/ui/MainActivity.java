package com.epicodus.movietime.ui;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.epicodus.movietime.R;
import com.epicodus.movietime.services.SearchService;

import java.util.List;

import info.movito.themoviedbapi.model.MovieDb;

public class MainActivity extends AppCompatActivity {
    private final String TAG = this.getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new GetMovieTask().execute();
    }

    private class GetMovieTask extends AsyncTask<Void, Void, List<MovieDb>> {
        @Override
        protected List<MovieDb> doInBackground(Void... params) {
            List<MovieDb> movies = SearchService.movieSearch("bananas", 0, 1);
            return movies;
        }

        @Override
        protected void onPostExecute(List<MovieDb> result) {
            TextView view = (TextView) findViewById(R.id.hello);
            String resultTitles = "";
            for (MovieDb movie : result) {
                resultTitles += movie.getTitle() + "\n";
            }
            view.setText(resultTitles);
        }
    }
}
