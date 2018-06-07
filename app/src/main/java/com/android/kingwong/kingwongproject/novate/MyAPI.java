package com.android.kingwong.kingwongproject.novate;

import com.android.kingwong.kingwongproject.novate.model.SouguBean;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

/**
 * Created by Tamic on 2016-07-07.
 */
public interface MyAPI {



    @GET("app.php")
    Observable<SouguBean> getSougu(@QueryMap Map<String, Object> maps);

    @GET("{url}")
    Observable<ResponseBody>getWeatherStr(@Path("url") String url,
                                          @QueryMap Map<String, String> maps);
}
