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

public class WorldFragment extends BaseArticlesFragment{
    private static final String LOG_TAG = WorldFragment.class.getName();

    @NonNull
    @Override
    public Loader<List<News>> onCreateLoader(int i, Bundle bundle) {
        String worldUrl = NewsPreferences.getPreferredUrl(getContext(), getString(R.string.world));
        Log.e(LOG_TAG, worldUrl);

        return new NewsLoader(getActivity(),worldUrl);
    }
}
