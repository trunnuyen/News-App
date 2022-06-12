package com.trng.znews.Fragments;

import com.trng.znews.Utils.Constants;

public class EnvironmentFragment extends BaseArticlesFragment{
    @Override
    public String getUrl() {
        return Constants.HEATH_NEWS_REQUEST_URL;
    }
    @Override
    public String getSection() {
        return Constants.sENVIRONMENT;
    }
}
