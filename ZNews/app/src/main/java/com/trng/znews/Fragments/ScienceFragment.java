package com.trng.znews.Fragments;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.loader.content.Loader;

import com.trng.znews.Models.News;
import com.trng.znews.Models.NewsLoader;
import com.trng.znews.Models.NewsPreferences;
import com.trng.znews.R;

import java.util.List;

public class ScienceFragment extends BaseArticlesFragment{
    private static final String LOG_TAG = ScienceFragment.class.getName();

    @NonNull
    @Override
    public Loader<List<News>> onCreateLoader(int i, Bundle bundle) {
        String scienceUrl = NewsPreferences.getPreferredUrl(getContext(), getString(R.string.science));
        Log.e(LOG_TAG, scienceUrl);

        // Create a new loader for the given URL
        return new NewsLoader(getActivity(),scienceUrl);
    }
}