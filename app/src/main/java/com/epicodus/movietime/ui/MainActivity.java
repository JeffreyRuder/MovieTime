package com.epicodus.movietime.ui;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.epicodus.movietime.R;
import com.epicodus.movietime.services.SearchService;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import info.movito.themoviedbapi.model.MovieDb;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private final String TAG = this.getClass().getSimpleName();
    @Bind(R.id.queryInputEditText) EditText mQueryInputEditText;
    @Bind(R.id.submitButton) Button mSubmitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mSubmitButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == mSubmitButton) {
            String searchQuery = mQueryInputEditText.getText().toString();
            Intent intent = new Intent(MainActivity.this, MovieListActivity.class);
            intent.putExtra("searchQuery", searchQuery);
            startActivity(intent);
        }
    }

}
