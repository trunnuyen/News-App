package com.trng.znews.Fragments;

import com.trng.znews.Utils.Constants;

public class FashionFragment extends BaseArticlesFragment{
    @Override
    public String getUrl() {
        return Constants.TRAVEL_NEWS_REQUEST_URL;
    }
    @Override
    public String getSection() {
        return Constants.sFASHION;
    }
}
