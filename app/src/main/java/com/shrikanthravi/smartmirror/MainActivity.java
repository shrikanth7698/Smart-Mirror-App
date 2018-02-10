package com.shrikanthravi.smartmirror;

import android.graphics.Typeface;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextClock;
import android.widget.TextView;

import com.shrikanthravi.smartmirror.data.model.Article;
import com.shrikanthravi.smartmirror.data.model.GlobalData;
import com.shrikanthravi.smartmirror.data.model.GoogleNewsIndia;
import com.shrikanthravi.smartmirror.data.model.Main;
import com.shrikanthravi.smartmirror.data.model.Sys;
import com.shrikanthravi.smartmirror.data.model.Weather;
import com.shrikanthravi.smartmirror.data.model.WeatherDetails;
import com.shrikanthravi.smartmirror.data.model.remote.APIService;
import com.shrikanthravi.smartmirror.data.model.remote.ApiUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    TextView dayofWeek,monthTv,temperature,tempDesc,sunriseTV,sunsetTV;
    TextClock textClock;
    APIService mApiService;
    RecyclerView newsRV;
    NewsAdapter newsAdapter;
    List<Article> newsList;
    int newspos=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        dayofWeek = (TextView) findViewById(R.id.dayofweek);
        monthTv =(TextView) findViewById(R.id.month);
        textClock = (TextClock) findViewById(R.id.textClock);
        temperature =(TextView) findViewById(R.id.temperature);
        tempDesc =(TextView) findViewById(R.id.temperatureDesc);
        sunriseTV =(TextView) findViewById(R.id.sunriseTime);
        sunsetTV =(TextView) findViewById(R.id.sunsetTime);
        newsRV =(RecyclerView) findViewById(R.id.newsRV);
        newsList = new ArrayList<>();
        newsAdapter = new NewsAdapter(newsList,getApplicationContext());

        LinearLayoutManager horizontalLayoutManager
                = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false);
        newsRV.setLayoutManager(horizontalLayoutManager);
        newsRV.setAdapter(newsAdapter);

        Typeface regular = Typeface.createFromAsset(getAssets(), "fonts/product_san_regular.ttf");
        FontChanger fontChanger = new FontChanger(regular);
        fontChanger.replaceFonts((ViewGroup)this.findViewById(android.R.id.content));

        Typeface bold = Typeface.createFromAsset(getAssets(), "fonts/product_sans_bold.ttf");
        temperature.setTypeface(bold);
        temperature.setText("29"+(char) 0x00B0);





        final Handler newsScrollHandler = new Handler();
        MainActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                Calendar sCalendar;
                sCalendar = Calendar.getInstance();
                String dayLongName = sCalendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault());
                dayofWeek.setText(dayLongName);
                String month = sCalendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());
                monthTv.setText(month+" "+sCalendar.get(Calendar.DATE));
                System.out.println(dayLongName+"  "+month);
                if(newsList.size()!=0) {
                    if(newspos<newsList.size()) {
                        newspos++;
                        newsRV.smoothScrollToPosition(newspos);
                    }
                    else{
                        newspos=0;
                        newsRV.smoothScrollToPosition(newspos);
                    }
                }
                newsScrollHandler.postDelayed(this,10*1000);
            }
        });

        final Handler weatherHandler = new Handler();
        MainActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                requestWeather();
                weatherHandler.postDelayed(this,30*60*1000);
            }
        });

        final Handler newsHandler = new Handler();
        MainActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                requestNews();
                newsHandler.postDelayed(this,30*60*1000);
            }
        });

    }

    public String convertUnixToNormal(long unixSeconds){

// convert seconds to milliseconds
        Date date = new Date(unixSeconds*1000L);
// the format of your date
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
// give a timezone reference for formatting (see comment at the bottom)
        //sdf.setTimeZone(TimeZone.getTimeZone("GMT-5:30"));
        String formattedDate = sdf.format(date);
        System.out.println(formattedDate);
        return formattedDate;
    }
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }

    public void requestWeather(){
        mApiService = ApiUtils.getAPIService();
        Call call = mApiService.weatherDetailsCall("");
        call.enqueue(new Callback<WeatherDetails>() {
            @Override
            public void onResponse(Call<WeatherDetails> call, Response<WeatherDetails> response) {

                System.out.println("Weather response code"+response.code());
                if (response.isSuccessful()) {

                    System.out.println("weather success");
                    System.out.println("temp "+response.body().getMain().getTemp().toString() +(char) 0x00B0);
                    temperature.setText(response.body().getMain().getTemp().toString()+(char) 0x00B0);
                    tempDesc.setText(response.body().getWeather().get(0).getMain().toString());
                    sunriseTV.setText(convertUnixToNormal(response.body().getSys().getSunrise()));
                    sunsetTV.setText(convertUnixToNormal(response.body().getSys().getSunset()));

                    try {

                    }

                    catch (Exception e) {
                        if (e.getMessage() != null) {

                        }
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onFailure(Call<WeatherDetails> call, Throwable t) {
                //Log.e(TAG, "Unable to submit post to API.");
                System.out.println("Weather request error");

            }
        });

    }

    public void requestNews(){
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .build();
        Retrofit retrofit = null;
        retrofit = new Retrofit.Builder()
                .baseUrl("https://newsapi.org/v2/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
        APIService mAPIService1;
        mAPIService1 = retrofit.create(APIService.class);
        Call call1 = mAPIService1.googleNewsIndiaCall("google-news-in", GlobalData.NewsApiKey);
        call1.enqueue(new Callback<GoogleNewsIndia>() {
            @Override
            public void onResponse(Call<GoogleNewsIndia> call, Response<GoogleNewsIndia> response) {

                System.out.println("news response code"+response.code()+" "+response.message());

                if (response.isSuccessful()) {

                    newsList.clear();
                    newsList.addAll(response.body().getArticles());
                    newsAdapter.notifyDataSetChanged();

                    try {

                    }

                    catch (Exception e) {
                        if (e.getMessage() != null) {

                        }
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onFailure(Call<GoogleNewsIndia> call, Throwable t) {
                //Log.e(TAG, "Unable to submit post to API.");
                System.out.println("news request error");

            }
        });
    }
}
