package com.trng.znews.Models;

import com.trng.znews.Utils.Constants;

import org.json.JSONException;
import org.json.JSONObject;

public class Weather {
    private String mTemperature,micon,mcity,mWeatherType;
    private int mCondition;

    public static Weather fromJson(JSONObject jsonObject)
    {
        try
        {
            Weather weatherD=new Weather();
            weatherD.mcity=jsonObject.getString("name");
            weatherD.mCondition=jsonObject.getJSONArray("weather").getJSONObject(0).getInt("id");
            weatherD.mWeatherType=jsonObject.getJSONArray("weather").getJSONObject(0).getString("main");
            weatherD.micon=updateWeatherIcon(weatherD.mCondition);
            double tempResult=jsonObject.getJSONObject("main").getDouble("temp")-273.15;
            int roundedValue=(int)Math.rint(tempResult);
            weatherD.mTemperature=Integer.toString(roundedValue);
            return weatherD;
        }
        catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    //Đổi ảnh hiển thị thời tiết
    private static String updateWeatherIcon(int condition)
    {
        if(condition>=0 && condition<=300)
        {
            return Constants.THUNDERSTORM_WEATHER;
        }
        else if(condition>=300 && condition<=500)
        {
            return Constants.LIGHT_RAIN_WEATHER;
        }
        else if(condition>=500 && condition<=600)
        {
            return Constants.SHOWER_WEATHER;
        }
        else  if(condition>=600 && condition<=700)
        {
            return Constants.SNOW2_WEATHER;
        }
        else if(condition>=701 && condition<=771)
        {
            return Constants.FOG_WEATHER;
        }

        else if(condition>=772 && condition<=800)
        {
            return Constants.OVERCAST_WEATHER;
        }
        else if(condition==800)
        {
            return Constants.SUNNY_WEATHER;
        }
        else if(condition>=801 && condition<=804)
        {
            return Constants.CLOUDY_WEATHER;
        }
        else  if(condition>=900 && condition<=902)
        {
            return Constants.THUNDERSTORM_WEATHER;
        }
        if(condition==903)
        {
            return Constants.SNOW1_WEATHER;
        }
        if(condition==904)
        {
            return Constants.SUNNY_WEATHER;
        }
        if(condition>=905 && condition<=1000)
        {
            return Constants.THUNDERSTORM_WEATHER;
        }
        return Constants.UNKNOWN_WEATHER;
    }

    public String getmTemperature() {
        return mTemperature+"°C";
    }

    public String getMicon() {
        return micon;
    }

    public String getMcity() {
        return mcity;
    }

    public String getmWeatherType() {
        return mWeatherType;
    }
}
