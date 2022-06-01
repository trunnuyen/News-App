package com.trng.znews.Fragments;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.trng.znews.Activities.EmptyRecyclerView;
import com.trng.znews.Adapters.NewsAdapter;
import com.trng.znews.Models.News;
import com.trng.znews.Models.NewsLoader;
import com.trng.znews.Models.NewsPreferences;
import com.trng.znews.R;
import com.trng.znews.Utils.Constants;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<News>> {
    private static final String LOG_TAG = SearchFragment.class.getName();
    private String KEY_WORLD;
    private SearchView search_view;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private NewsAdapter mAdapter;
    private TextView mEmptyStateTextView;
    private View mLoadingIndicator;
    private static final int NEWS_LOADER_ID = 1;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_search, container, false);
        search_view = rootView.findViewById(R.id.search_view);

        search_view.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                KEY_WORLD = s;
                initiateRefresh();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return true;
            }
        });

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

        Uri.Builder uriBuilder = NewsPreferences.getSearchUrl(getContext(),KEY_WORLD);

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
