package com.trng.znews.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Html;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.trng.znews.Activities.NewsDetailActivity;
import com.trng.znews.LocalData.DatabaseHandler;
import com.trng.znews.Models.News;
import com.trng.znews.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import com.squareup.picasso.Picasso;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder>{
    private Context mContext;
    private List<News> mNewsList;
    private SharedPreferences sharedPrefs;
    public static DatabaseHandler databaseArticleWasRead;
    public static DatabaseHandler databaseSavedArticle;

    public NewsAdapter(Context context, List<News> newsList) {
        mContext = context;
        mNewsList = newsList;
    }

    @NonNull
    @Override
    public NewsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_card_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsAdapter.ViewHolder holder, int position) {
        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(mContext);

        final News currentNews = mNewsList.get(position);

        holder.titleTextView.setText(currentNews.getTitle());
        // N???u ko c?? t??n t??c gi??? th?? ???n authortextview
        if (currentNews.getAuthor() == null) {
            holder.authorTextView.setVisibility(View.GONE);
        } else {
            holder.authorTextView.setVisibility(View.VISIBLE);
            holder.authorTextView.setText(currentNews.getAuthor());
        }

        holder.sectionTextView.setText(currentNews.getSection());

        holder.dateTextView.setText(getTimeDifference(formatDate(currentNews.getDate())));

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Convert the String URL into a URI object (to pass into the Intent constructor)
                Uri newsUri = Uri.parse(currentNews.getUrl());
                // Create a new intent to view the news URI
                Intent intent = new Intent(mContext, NewsDetailActivity.class);
                // Send the intent to launch a new activity
                Bundle bundle = new Bundle();
                bundle.putString("link" , String.valueOf(newsUri));
                intent.putExtras(bundle);
                mContext.startActivity(intent);
            }
        });

        holder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
                alertDialog.setTitle("     X??c Nh???n...");
                alertDialog.setMessage("B???n c?? th???c s??? mu???n x??a tin n??y!");
                alertDialog.setIcon(R.drawable.ic_cloud);
                alertDialog.setCancelable(false);
                alertDialog.setPositiveButton("C??", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i2) {
                        databaseSavedArticle.QueryData("DELETE FROM contacts WHERE link='" + currentNews.getUrl() + "'");
                        mNewsList.remove(currentNews);
                        notifyDataSetChanged();
                        dialogInterface.dismiss();
                    }
                });
                alertDialog.setNeutralButton("Kh??ng", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                alertDialog.show();
                return true;
            }
        });

        if (currentNews.getThumbnail() == null) {
            holder.thumbnailImageView.setVisibility(View.GONE);
        } else {
            holder.thumbnailImageView.setVisibility(View.VISIBLE);

            Picasso.with(mContext.getApplicationContext())
                    .load(currentNews.getThumbnail())
                    .into(holder.thumbnailImageView);
        }

        holder.shareImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareData(currentNews);
            }
        });

        //t???o database
        databaseArticleWasRead = new DatabaseHandler(mContext, "TinDaDoc.sqlite", null, 1);
        databaseArticleWasRead.QueryData("CREATE TABLE IF NOT EXISTS contacts(id INTEGER PRIMARY KEY AUTOINCREMENT, title NVARCHAR(100),link VARCHAR(100))");

        databaseSavedArticle = new DatabaseHandler(mContext, "TinDaLuu.sqlite", null, 1);
        databaseSavedArticle.QueryData("CREATE TABLE IF NOT EXISTS contacts(id INTEGER PRIMARY KEY AUTOINCREMENT, img NVARCHAR(100),title NVARCHAR(100),link VARCHAR(100),date NVARCHAR(20))");

        holder.saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    databaseSavedArticle.QueryData("INSERT INTO contacts VALUES(null,'" + currentNews.getThumbnail() + "','" + currentNews.getTitle() + "','" + currentNews.getUrl() + "','" + currentNews.getDate() + "')");
                    Toast.makeText(view.getContext(), "???? L??u", Toast.LENGTH_SHORT).show();
                    notifyDataSetChanged();
            }
        });
    }

    //l???y url ????? share news
    private void shareData(News news) {
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT,
                news.getTitle() + " : " + news.getUrl());
        mContext.startActivity(Intent.createChooser(sharingIntent,
                mContext.getString(R.string.share_article)));
    }

    private String formatDate(String dateStringUTC) {
        // Parse the dateString into a Date object
        SimpleDateFormat simpleDateFormat =
                new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z");
        Date dateObject = null;
        try {
            dateObject = simpleDateFormat.parse(dateStringUTC);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        SimpleDateFormat df = new SimpleDateFormat("MMM d, yyyy  h:mm a", Locale.ENGLISH);
        String formattedDateUTC = df.format(dateObject);

        Date date = null;
        try {
            date = df.parse(formattedDateUTC);
            df.setTimeZone(TimeZone.getDefault());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return df.format(date);
    }

    private CharSequence getTimeDifference(String formattedDate) {
        long currentTime = System.currentTimeMillis();
        long publicationTime = getDateInMillis(formattedDate);
        return DateUtils.getRelativeTimeSpanString(publicationTime, currentTime,
                DateUtils.SECOND_IN_MILLIS);
    }

    private static long getDateInMillis(String formattedDate) {
        SimpleDateFormat simpleDateFormat =
                new SimpleDateFormat("MMM d, yyyy  h:mm a");
        long dateInMillis;
        Date dateObject;
        try {
            dateObject = simpleDateFormat.parse(formattedDate);
            dateInMillis = dateObject.getTime();
            return dateInMillis;
        } catch (ParseException e) {
            Log.e("Problem parsing date", e.getMessage());
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public int getItemCount() {
        return mNewsList.size();
    }

    public void clearAll() {
        mNewsList.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<News> newsList) {
        mNewsList.clear();
        mNewsList.addAll(newsList);
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView titleTextView;
        private TextView authorTextView;
        private TextView sectionTextView;
        private TextView dateTextView;
        private ImageView thumbnailImageView;
        private ImageView shareImageView;
        private CardView cardView;
        private ImageView saveBtn;

        ViewHolder(View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.title_card);
            authorTextView = itemView.findViewById(R.id.author_card);
            sectionTextView = itemView.findViewById(R.id.section_card);
            dateTextView = itemView.findViewById(R.id.date_card);
            thumbnailImageView = itemView.findViewById(R.id.thumbnail_image_card);
            shareImageView = itemView.findViewById(R.id.share_image_card);
            cardView = itemView.findViewById(R.id.card_view);
            saveBtn = itemView.findViewById(R.id.save_news_button);
            saveBtn.setTag(R.drawable.ic_bookmark_empty);
        }
    }
}
