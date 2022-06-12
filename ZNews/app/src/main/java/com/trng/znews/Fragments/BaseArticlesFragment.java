package com.trng.znews.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.trng.znews.Activities.EmptyRecyclerView;
import com.trng.znews.Activities.SignInActivity;
import com.trng.znews.Adapters.NewsAdapter;
import com.trng.znews.Models.News;
import com.trng.znews.R;
import com.trng.znews.Utils.Constants;

import org.jsoup.Jsoup;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseArticlesFragment extends Fragment {

    private NewsAdapter mAdapter;

    private TextView mEmptyStateTextView;

    private SwipeRefreshLayout mSwipeRefreshLayout;

    List<News> arrArticle= new ArrayList<>();
    List<News> arrArticle2= new ArrayList<>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser == null) {
            Intent intent = new Intent(getActivity(), SignInActivity.class);
            startActivity(intent);
        }

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

        mEmptyStateTextView = rootView.findViewById(R.id.empty_view);
        mRecyclerView.setEmptyView(mEmptyStateTextView);

        mAdapter = new NewsAdapter(getActivity(), arrArticle);

        mRecyclerView.setAdapter(mAdapter);
        initializeLoader(isNetworkAvailable());
        //new ReadDataFromURL().execute(Constants.EX_NEWS_REQUEST_URL);
        return rootView;
    }

    class ReadDataFromURL extends AsyncTask<String, Void, String> {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            arrArticle.clear();
            mAdapter.notifyDataSetChanged();
            arrArticle2.clear();

            dialog = new ProgressDialog(getContext());
            dialog.setMessage("      Loading...");
            dialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            //  ArrayList<NewsModel> arr=new ArrayList<NewsModel>();
            String url = strings[0];
            //   arrArticle.clear();
            try {
                org.jsoup.nodes.Document doc = Jsoup.connect(url).get();
                Elements elements = doc.select("item");
                for (org.jsoup.nodes.Element item : elements) {
                    String title = item.select("title").text();
                    String link = item.select("link").text();
                    String date = item.select("pubDate").text();
                    String des = item.select("description").text();
                    //     Log.d("des",des);
                    org.jsoup.nodes.Document docImage = Jsoup.parse(des);
                    String image = docImage.select("img").get(0).attr("src");
                    //  Log.d("img",image);
                    Log.d("link", link);
                    arrArticle2.add(new News(title,  Constants.VNEXPRESS, getSection(), date, link, image));
                    Log.d("arr", "" + arrArticle.size());
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.d("error ", "" + e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            arrArticle.addAll(arrArticle2);
            mAdapter.notifyDataSetChanged();

            dialog.dismiss();
            super.onPostExecute(s);

            Log.d("arr", "...." + arrArticle.size());
        }
    }

    //Kiem tra mang
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }

    @Override
    public void onResume() {
        super.onResume();
        restartLoader(isNetworkAvailable());
    }

    void initializeLoader(boolean isConnected) {
        if (isConnected) {
            new ReadDataFromURL().execute(getUrl());

        mEmptyStateTextView.setText(R.string.no_news);

        mAdapter.clearAll();

        if (arrArticle != null && !arrArticle.isEmpty()) {
            mAdapter.addAll(arrArticle);
        }
        mSwipeRefreshLayout.setRefreshing(false);
        } else {

            mEmptyStateTextView.setText(R.string.no_internet_connection);
            mEmptyStateTextView.setCompoundDrawablesWithIntrinsicBounds(Constants.DEFAULT_NUMBER,
                    R.drawable.ic_network_check,Constants.DEFAULT_NUMBER,Constants.DEFAULT_NUMBER);
        }
    }

    private void restartLoader(boolean isConnected) {
        if (isConnected) {
            mAdapter.clearAll();
            new ReadDataFromURL().execute(getUrl());
            mSwipeRefreshLayout.setRefreshing(false);

        } else {
            mEmptyStateTextView.setText(R.string.no_internet_connection);
            mEmptyStateTextView.setCompoundDrawablesWithIntrinsicBounds(Constants.DEFAULT_NUMBER,
                    R.drawable.ic_network_check,Constants.DEFAULT_NUMBER,Constants.DEFAULT_NUMBER);

            mSwipeRefreshLayout.setVisibility(View.GONE);
        }
    }

    public String getUrl(){
        return Constants.EX_NEWS_REQUEST_URL;
    }

    public String getSection() {
        return Constants.sNEWEST;
    }

    void initiateRefresh() {
        restartLoader(isNetworkAvailable());
    }
}