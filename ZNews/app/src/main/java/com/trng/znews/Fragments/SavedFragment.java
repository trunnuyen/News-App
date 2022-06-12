package com.trng.znews.Fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.trng.znews.Activities.EmptyRecyclerView;
import com.trng.znews.Adapters.NewsAdapter;
import com.trng.znews.LocalData.DatabaseHandler;
import com.trng.znews.Models.News;
import com.trng.znews.R;
import com.trng.znews.Utils.Constants;

import java.util.ArrayList;
import java.util.List;

public class SavedFragment extends Fragment {

    private ImageView img;
    private NewsAdapter mAdapter;
    private TextView mEmptyStateTextView;

    public static DatabaseHandler databaseSavedArticle;

    List<News> arrArticle = new ArrayList<>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        EmptyRecyclerView mRecyclerView = rootView.findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setHasFixedSize(true);

        mRecyclerView.setLayoutManager(layoutManager);

        databaseSavedArticle = new DatabaseHandler(getContext(), "TinDaLuu.sqlite", null, 1);
        databaseSavedArticle.QueryData("CREATE TABLE IF NOT EXISTS contacts(id INTEGER PRIMARY KEY AUTOINCREMENT, img NVARCHAR(100),title NVARCHAR(100),link VARCHAR(100),date NVARCHAR(20))");

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

        //new ReadDataFromURL().execute(Constants.EX_NEWS_REQUEST_URL);

        getSavedArticleFromDatabase();
        return rootView;
    }

    private void getSavedArticleFromDatabase(){
        arrArticle.clear();
        Cursor dataContacts = databaseSavedArticle.GetData("SELECT * FROM contacts");
        while (dataContacts.moveToNext()) {    //khi con` du lieu
            int id = dataContacts.getInt(0);

            String img = dataContacts.getString(1);
            String title = dataContacts.getString(2); //cot 1
            String link = dataContacts.getString(3);
            String date = dataContacts.getString(4);
            arrArticle.add(0, new News(id, title, Constants.sSAVED, Constants.VNEXPRESS, date, link, img));
            mAdapter.notifyDataSetChanged();
        }
        mEmptyStateTextView.setText(R.string.no_saved);
    }
}