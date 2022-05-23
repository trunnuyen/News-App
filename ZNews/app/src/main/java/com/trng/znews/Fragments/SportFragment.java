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

public class SportFragment extends BaseArticlesFragment{
    private static final String LOG_TAG = SportFragment.class.getName();

    @NonNull
    @Override
    public Loader<List<News>> onCreateLoader(int i, Bundle bundle) {
        String sportUrl = NewsPreferences.getPreferredUrl(getContext(), getString(R.string.sport));
        Log.e(LOG_TAG, sportUrl);

        // Create a new loader for the given URL
        return new NewsLoader(getActivity(),sportUrl);
    }
}
