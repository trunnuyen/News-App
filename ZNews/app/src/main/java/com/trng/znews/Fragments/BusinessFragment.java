package com.trng.znews.Fragments;

import com.trng.znews.Utils.Constants;

public class BusinessFragment extends BaseArticlesFragment{
    @Override
    public String getUrl() {
        return Constants.BUSINESS_NEWS_REQUEST_URL;
    }

    @Override
    public String getSection() {
        return Constants.sBUSINESS;
    }
}