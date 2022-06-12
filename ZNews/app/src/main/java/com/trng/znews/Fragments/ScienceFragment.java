package com.trng.znews.Fragments;

import com.trng.znews.Utils.Constants;

public class ScienceFragment extends BaseArticlesFragment{
    @Override
    public String getUrl() {
        return Constants.SCIENCE_NEWS_REQUEST_URL;
    }

    @Override
    public String getSection() {
        return Constants.sSCIENCE;
    }
}
