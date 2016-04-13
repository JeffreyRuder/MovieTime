package com.jtrmb.movietime.ui;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.view.MenuCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.jtrmb.movietime.R;
import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    @Bind(R.id.backgroundImageView) ImageView mBackgroundImageView;
    @Bind(R.id.aboutButton) Button mAboutButton;
    @Bind(R.id.tagSearchTextView) TextView mTagSearchTextView;
    @Bind(R.id.tagShareTextView) TextView mTagShareTextView;
    @Bind(R.id.tagSaveTextView) TextView mTagSaveTextView;

    private SearchView mSearchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        Typeface pacifico = Typeface.createFromAsset(getAssets(), "fonts/Pacifico.ttf");
        mTagSearchTextView.setTypeface(pacifico);
        mTagShareTextView.setTypeface(pacifico);
        mTagSaveTextView.setTypeface(pacifico);

        Picasso.with(this).load(R.drawable.background).fit().centerCrop().into(mBackgroundImageView);
        mAboutButton.setOnClickListener(this);
        mTagSearchTextView.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        ButterKnife.bind(this);

        MenuItem menuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        mSearchView = searchView;
        mSearchView.setQueryHint(getString(R.string.search_movies));
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Intent intent = new Intent(MainActivity.this, MovieListActivity.class);
                intent.putExtra("searchQuery", query);
                startActivity(intent);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return true;
    }

    @Override
    public void onClick(View v) {
        if (v == mAboutButton) {
            Intent intent = new Intent(MainActivity.this, AboutActivity.class);
            startActivity(intent);
        }
        if (v == mTagSearchTextView) {
            mSearchView.setIconified(false);
        }
    }
}
