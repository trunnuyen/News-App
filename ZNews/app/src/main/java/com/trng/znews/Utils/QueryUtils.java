package com.trng.znews.Utils;

import android.text.TextUtils;
import android.util.Log;

import com.trng.znews.Models.News;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class QueryUtils {

    private static final String LOG_TAG = QueryUtils.class.getSimpleName();

    private QueryUtils() {
    }

    //Truy vấn dũ liệu từ Gủardian và trả về danh sách các news objects
    public static List<News> fetchNewsData(String requestUrl) {

        URL url = createUrl(requestUrl);

        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        // Extract relevant fields from the JSON response and create a list of {@link News}
        List<News> newsList = extractFeatureFromJSON(jsonResponse);

        // Return the list of {@link News}
        return newsList;
    }

    //Tạo đối tượng kiểu URL mới từ chuỗi String url
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL.", e);
        }
        return url;
    }

    //Tạo HTTP request từ URL và trả về chuỗi JSON
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(Constants.READ_TIMEOUT /* milliseconds */);
            urlConnection.setConnectTimeout(Constants.CONNECT_TIMEOUT /* milliseconds */);
            urlConnection.setRequestMethod(Constants.REQUEST_METHOD_GET);
            urlConnection.connect();

            // Request thành công SUCCESS_RESPONSE_CODE = 200
            if (urlConnection.getResponseCode() == Constants.SUCCESS_RESPONSE_CODE) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the news JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    //Chuyển đổi InputStream thành String
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    //Trả về News list từ chuỗi JSON
    private static List<News> extractFeatureFromJSON(String newsJSON) {

        if (TextUtils.isEmpty(newsJSON)) {
            return null;
        }

        List<News> newsList = new ArrayList<>();

        try {
            // Tạo JSONObject từ chuỗi json
            JSONObject baseJsonResponse = new JSONObject(newsJSON);

            JSONObject responseJsonObject = baseJsonResponse.getJSONObject(Constants.JSON_KEY_RESPONSE);

            JSONArray resultsArray = responseJsonObject.getJSONArray(Constants.JSON_KEY_RESULTS);

            for (int i = 0; i < resultsArray.length(); i++) {

                // lấy curent news ở vị trí i
                JSONObject currentNews = resultsArray.getJSONObject(i);
                // lấy web title
                String webTitle = currentNews.getString(Constants.JSON_KEY_WEB_TITLE);
                // lấy tên danh mục (section name)
                String sectionName = currentNews.getString(Constants.JSON_KEY_SECTION_NAME);
                // lấy ngày đăng
                String webPublicationDate = currentNews.getString(Constants.JSON_KEY_WEB_PUBLICATION_DATE);
                // lấy web url
                String webUrl = currentNews.getString(Constants.JSON_KEY_WEB_URL);

                // nếu news trả về có tags
                String author = null;
                if (currentNews.has(Constants.JSON_KEY_TAGS)) {
                    // lất tags
                    JSONArray tagsArray = currentNews.getJSONArray(Constants.JSON_KEY_TAGS);
                    if (tagsArray.length() != 0) {
                        // lấy JSONObject đầu trong tags
                        JSONObject firstTagsItem = tagsArray.getJSONObject(0);
                        // lấy tên tác giả từ web title nằm trong tags
                        author = firstTagsItem.getString(Constants.JSON_KEY_WEB_TITLE);
                    }
                }

                // nếu news trả về có fields
                String thumbnail = null;
                String trailText = null;
                if (currentNews.has(Constants.JSON_KEY_FIELDS)) {

                    JSONObject fieldsObject = currentNews.getJSONObject(Constants.JSON_KEY_FIELDS);
                    // Lấy thumbnail
                    if (fieldsObject.has(Constants.JSON_KEY_THUMBNAIL)) {
                        thumbnail = fieldsObject.getString(Constants.JSON_KEY_THUMBNAIL);
                    }
                    // Lấy phần text mô tả ngắn
                    if (fieldsObject.has(Constants.JSON_KEY_TRAIL_TEXT)) {
                        trailText = fieldsObject.getString(Constants.JSON_KEY_TRAIL_TEXT);
                    }
                }

                News news = new News(webTitle, sectionName, author, webPublicationDate, webUrl, thumbnail, trailText);

                newsList.add(news);
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem parsing the news JSON results", e);
        }

        return newsList;
    }
}
