package com.trng.znews.Fragments;

import com.trng.znews.Utils.Constants;

public class SportFragment extends BaseArticlesFragment{
    @Override
    public String getUrl() {
        return Constants.SPORT_NEWS_REQUEST_URL;
    }

    @Override
    public String getSection() {
        return Constants.sSPORT;
    }

}
