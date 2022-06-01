package com.trng.znews.Utils;

public class Constants {
    private Constants() {
    }

    static final String JSON_KEY_RESPONSE = "response";
    static final String JSON_KEY_RESULTS = "results";
    static final String JSON_KEY_WEB_TITLE = "webTitle";
    static final String JSON_KEY_SECTION_NAME = "sectionName";
    static final String JSON_KEY_WEB_PUBLICATION_DATE = "webPublicationDate";
    static final String JSON_KEY_WEB_URL = "webUrl";
    static final String JSON_KEY_TAGS = "tags";
    static final String JSON_KEY_FIELDS = "fields";
    static final String JSON_KEY_THUMBNAIL = "thumbnail";
    static final String JSON_KEY_TRAIL_TEXT = "trailText";

    static final int READ_TIMEOUT = 10000; /* milliseconds */

    static final int CONNECT_TIMEOUT = 15000; /* milliseconds */

    static final int SUCCESS_RESPONSE_CODE = 200;

    static final String REQUEST_METHOD_GET = "GET";

    //News data from guardians API
    public static final String NEWS_REQUEST_URL = "https://content.guardianapis.com/search";

    public static final String QUERY_PARAM = "q";
    public static final String ORDER_BY_PARAM = "order-by";
    public static final String PAGE_SIZE_PARAM = "page-size";
    public static final String ORDER_DATE_PARAM = "order-date";
    public static final String FROM_DATE_PARAM = "from-date";
    public static final String SHOW_FIELDS_PARAM = "show-fields";
    public static final String FORMAT_PARAM = "format";
    public static final String SHOW_TAGS_PARAM = "show-tags";
    public static final String API_KEY_PARAM = "api-key";
    public static final String SECTION_PARAM = "section";
    public static final String SEARCH_PARAM = "search";

    public static final String SHOW_FIELDS = "thumbnail,trailText";

    public static final String FORMAT = "json";

    public static final String SHOW_TAGS = "contributor";

    public static final String API_KEY = "test";

    public static  final String THUNDERSTORM_WEATHER = "thunderstorm2";
    public static  final String LIGHT_RAIN_WEATHER = "lightrain";
    public static  final String SHOWER_WEATHER = "shower";
    public static  final String SNOW2_WEATHER = "snow2";
    public static  final String FOG_WEATHER = "fog";
    public static  final String OVERCAST_WEATHER = "overcast";
    public static  final String SUNNY_WEATHER = "sunny";
    public static  final String CLOUDY_WEATHER = "cloudy";
    public static  final String SNOW1_WEATHER = "snow1";
    public static  final String UNKNOWN_WEATHER = "Unknown";

    public static final int DEFAULT_NUMBER = 0;

    public static final int HOME = 0;
    public static final int WORLD = 1;
    public static final int SCIENCE = 2;
    public static final int SPORT = 3;
    public static final int ENVIRONMENT = 4;
    public static final int SOCIETY = 5;
    public static final int FASHION = 6;
    public static final int BUSINESS = 7;
    public static final int CULTURE = 8;
    public static final int SEARCH = 9;
    public static final int WEATHER = 10;
}
