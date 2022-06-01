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

public class CultureFragment extends BaseArticlesFragment {
    private static final String LOG_TAG = CultureFragment.class.getName();

    @NonNull
    @Override
    public Loader<List<News>> onCreateLoader(int i, Bundle bundle) {
        String cultureUrl = NewsPreferences.getPreferredUrl(getContext(), getString(R.string.culture));
        Log.e(LOG_TAG, cultureUrl);

        return new NewsLoader(getActivity(),cultureUrl);
    }
}
