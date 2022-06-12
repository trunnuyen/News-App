package com.trng.znews.Fragments;

import com.trng.znews.Utils.Constants;

public class WorldFragment extends BaseArticlesFragment{
    @Override
    public String getUrl() {
        return Constants.WORLD_NEWS_REQUEST_URL;
    }

    @Override
    public String getSection() {
        return Constants.sWORLD;
    }

}
