package com.trng.znews.Fragments;

import com.trng.znews.Utils.Constants;

public class SocietyFragment extends BaseArticlesFragment{
    @Override
    public String getUrl() {
        return Constants.SOCIETY_NEWS_REQUEST_URL;
    }

    @Override
    public String getSection() {
        return Constants.sSOCIETY;
    }

}
