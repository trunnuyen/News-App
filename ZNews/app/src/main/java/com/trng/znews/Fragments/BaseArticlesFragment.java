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

        // Find a reference to the {@link RecyclerView} in the layout
        // Replaced RecyclerView with EmptyRecyclerView
        EmptyRecyclerView mRecyclerView = rootView.findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setHasFixedSize(true);

        // Set the layoutManager on the {@link RecyclerView}
        mRecyclerView.setLayoutManager(layoutManager);

        // Find the SwipeRefreshLayout
        mSwipeRefreshLayout = rootView.findViewById(R.id.swipe_refresh);
        // Set the color scheme of the SwipeRefreshLayout
        mSwipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.swipe_color_1),
                getResources().getColor(R.color.swipe_color_2),
                getResources().getColor(R.color.swipe_color_3),
                getResources().getColor(R.color.swipe_color_4));

        // Set up OnRefreshListener that is invoked when the user performs a swipe-to-refresh gesture.
        mSwipeRefreshLayout.setOnRefreshListener(() -> {
            Log.i(LOG_TAG, "onRefresh called from SwipeRefreshLayout");
            // restart the loader
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
                    }, 3000); // delay of 3 seconds before hiding the fab

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
                    }, 2000); // delay of 2 seconds before hiding the fab
                }

            }
        });

        // Find the loading indicator from the layout
        mLoadingIndicator = rootView.findViewById(R.id.loading_indicator);

        // Find the empty view from the layout and set it on the new recycler view
        mEmptyStateTextView = rootView.findViewById(R.id.empty_view);
        mRecyclerView.setEmptyView(mEmptyStateTextView);

        // Create a new adapter that takes an empty list of news as input
        mAdapter = new NewsAdapter(getActivity(), new ArrayList<News>());

        // Set the adapter on the {@link recyclerView}
        mRecyclerView.setAdapter(mAdapter);

        // Check for network connectivity and initialize the loader
        initializeLoader(isConnected());

        return rootView;
    }


    @NonNull
    @Override
    public Loader<List<News>> onCreateLoader(int i, Bundle bundle) {

        Uri.Builder uriBuilder = NewsPreferences.getPreferredUri(getContext(),"");

        Log.e(LOG_TAG,uriBuilder.toString());

        // Create a new loader for the given URL
        return new NewsLoader(getActivity(), uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<News>> loader, List<News> newsData) {
        // Hide loading indicator because the data has been loaded
        mLoadingIndicator.setVisibility(View.GONE);

        // Set empty state text to display "No news found."
        mEmptyStateTextView.setText(R.string.no_news);

        // Clear the adapter of previous news data
        mAdapter.clearAll();

        // If there is a valid list of {@link News}, then add them to the adapter's
        // data set. This will trigger the recyclerView to update.
        if (newsData != null && !newsData.isEmpty()) {
            mAdapter.addAll(newsData);
        }

        // Hide the swipe icon animation when the loader is done refreshing the data
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<News>> loader) {
        // Loader reset, so we can clear out our existing data.
        mAdapter.clearAll();
    }

    /**
     * When the user returns to the previous screen by pressing the up button in the SettingsActivity,
     * restart the Loader to reflect the current value of the preference.
     */
    @Override
    public void onResume() {
        super.onResume();
        restartLoader(isConnected());
    }

    /**
     *  Check for network connectivity.
     */
    boolean isConnected() {
        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connectivityManager = (ConnectivityManager)
                getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get details on the currently active default data network
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        return (networkInfo != null && networkInfo.isConnected());
    }

    /**
     * If there is internet connectivity, initialize the loader as
     * usual. Otherwise, hide loading indicator and set empty state TextView to display
     * "No internet connection."
     *
     * @param isConnected internet connection is available or not
     */
    void initializeLoader(boolean isConnected) {
        if (isConnected) {
            // Get a reference to the LoaderManager, in order to interact with loaders.
            LoaderManager loaderManager = getLoaderManager();
            // Initialize the loader with the NEWS_LOADER_ID
            loaderManager.initLoader(NEWS_LOADER_ID, null, this);
        } else {
            // Otherwise, display error
            // First, hide loading indicator so error message will be visible
            mLoadingIndicator.setVisibility(View.GONE);
            // Update empty state with no connection error message and image
            mEmptyStateTextView.setText(R.string.no_internet_connection);
            mEmptyStateTextView.setCompoundDrawablesWithIntrinsicBounds(Constants.DEFAULT_NUMBER,
                    R.drawable.ic_network_check,Constants.DEFAULT_NUMBER,Constants.DEFAULT_NUMBER);
        }
    }

    /**
     * Restart the loader if there is internet connectivity.
     * @param isConnected internet connection is available or not
     */
    private void restartLoader(boolean isConnected) {
        if (isConnected) {
            // Get a reference to the LoaderManager, in order to interact with loaders.
            LoaderManager loaderManager = getLoaderManager();
            // Restart the loader with the NEWS_LOADER_ID
            loaderManager.restartLoader(NEWS_LOADER_ID, null, this);
        } else {
            // Otherwise, display error
            // First, hide loading indicator so error message will be visible
            mLoadingIndicator.setVisibility(View.GONE);
            // Update empty state with no connection error message and image
            mEmptyStateTextView.setText(R.string.no_internet_connection);
            mEmptyStateTextView.setCompoundDrawablesWithIntrinsicBounds(Constants.DEFAULT_NUMBER,
                    R.drawable.ic_network_check,Constants.DEFAULT_NUMBER,Constants.DEFAULT_NUMBER);

            // Hide SwipeRefreshLayout
            mSwipeRefreshLayout.setVisibility(View.GONE);
        }
    }

    /**
     * When the user performs a swipe-to-refresh gesture, restart the loader.
     */
    void initiateRefresh() {
        restartLoader(isConnected());
    }
}