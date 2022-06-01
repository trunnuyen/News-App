package com.trng.znews.Fragments;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.trng.znews.Activities.EmptyRecyclerView;
import com.trng.znews.Adapters.NewsAdapter;
import com.trng.znews.Models.News;
import com.trng.znews.Models.NewsLoader;
import com.trng.znews.Models.NewsPreferences;
import com.trng.znews.R;
import com.trng.znews.Utils.Constants;

import java.util.ArrayList;
import java.util.List;

public class BaseArticlesFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<News>> {

    private static final String LOG_TAG = BaseArticlesFragment.class.getName();

    private static final int NEWS_LOADER_ID = 1;

    private NewsAdapter mAdapter;

    private TextView mEmptyStateTextView;

    private View mLoadingIndicator;

    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        EmptyRecyclerView mRecyclerView = rootView.findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setHasFixedSize(true);

        mRecyclerView.setLayoutManager(layoutManager);

        mSwipeRefreshLayout = rootView.findViewById(R.id.swipe_refresh);
        mSwipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.swipe_color_1),
                getResources().getColor(R.color.swipe_color_2),
                getResources().getColor(R.color.swipe_color_3),
                getResources().getColor(R.color.swipe_color_4));

        mSwipeRefreshLayout.setOnRefreshListener(() -> {
            Log.i(LOG_TAG, "onRefresh called from SwipeRefreshLayout");

            initiateRefresh();
            Toast.makeText(getActivity(), getString(R.string.updated_just_now),
                    Toast.LENGTH_SHORT).show();
        });

        FloatingActionButton fab = rootView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//          recyclerView.scrollToPosition(0);
                mRecyclerView.smoothScrollToPosition(0);
            }
        });

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {

                if (dy > 0) { // scrolling down
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            fab.setVisibility(View.GONE);
                        }
                    }, 3000); // delay of 3 seconds before hiding the btn

                } else if (dy < 0) { // scrolling up

                    fab.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) { // No scrolling
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            fab.setVisibility(View.GONE);
                        }
                    }, 2000); // delay of 2 seconds before hiding the btn
                }

            }
        });


        mLoadingIndicator = rootView.findViewById(R.id.loading_indicator);

        mEmptyStateTextView = rootView.findViewById(R.id.empty_view);
        mRecyclerView.setEmptyView(mEmptyStateTextView);

        mAdapter = new NewsAdapter(getActivity(), new ArrayList<News>());

        mRecyclerView.setAdapter(mAdapter);

        initializeLoader(isConnected());

        return rootView;
    }


    @NonNull
    @Override
    public Loader<List<News>> onCreateLoader(int i, Bundle bundle) {

        Uri.Builder uriBuilder = NewsPreferences.getPreferredUri(getContext(),"");

        Log.e(LOG_TAG,uriBuilder.toString());

        return new NewsLoader(getActivity(), uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<News>> loader, List<News> newsData) {

        mLoadingIndicator.setVisibility(View.GONE);

        mEmptyStateTextView.setText(R.string.no_news);

        mAdapter.clearAll();

        if (newsData != null && !newsData.isEmpty()) {
            mAdapter.addAll(newsData);
        }

        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<News>> loader) {
        mAdapter.clearAll();
    }

    @Override
    public void onResume() {
        super.onResume();
        restartLoader(isConnected());
    }

    boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager)
                getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        return (networkInfo != null && networkInfo.isConnected());
    }

    void initializeLoader(boolean isConnected) {
        if (isConnected) {

            LoaderManager loaderManager = getLoaderManager();

            loaderManager.initLoader(NEWS_LOADER_ID, null, this);
        } else {
            mLoadingIndicator.setVisibility(View.GONE);

            mEmptyStateTextView.setText(R.string.no_internet_connection);
            mEmptyStateTextView.setCompoundDrawablesWithIntrinsicBounds(Constants.DEFAULT_NUMBER,
                    R.drawable.ic_network_check,Constants.DEFAULT_NUMBER,Constants.DEFAULT_NUMBER);
        }
    }

    //restart loader khi thiết bị kết nối với mạng
    private void restartLoader(boolean isConnected) {
        if (isConnected) {
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.restartLoader(NEWS_LOADER_ID, null, this);
        } else {
            mLoadingIndicator.setVisibility(View.GONE);

            mEmptyStateTextView.setText(R.string.no_internet_connection);
            mEmptyStateTextView.setCompoundDrawablesWithIntrinsicBounds(Constants.DEFAULT_NUMBER,
                    R.drawable.ic_network_check,Constants.DEFAULT_NUMBER,Constants.DEFAULT_NUMBER);

            mSwipeRefreshLayout.setVisibility(View.GONE);
        }
    }

    void initiateRefresh() {
        restartLoader(isConnected());
    }
}