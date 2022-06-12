package com.trng.znews.Fragments;

import com.trng.znews.Utils.Constants;

public class CultureFragment extends BaseArticlesFragment{
    @Override
    public String getUrl() {
        return Constants.EDUCATION_NEWS_REQUEST_URL;
    }

    @Override
    public String getSection() {
        return Constants.sCULTURE;
    }
}