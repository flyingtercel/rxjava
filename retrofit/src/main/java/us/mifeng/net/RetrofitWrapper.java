package us.mifeng.net;

import android.content.Context;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by 黑夜之火 on 2017/4/3.
 */

public class RetrofitWrapper {
    private static RetrofitWrapper instance;
    private Context mContext;
    private Retrofit retrofit;

    private RetrofitWrapper() {
        retrofit = new Retrofit.Builder().baseUrl(Constant.BASE_URL).addConverterFactory(
                GsonConverterFactory.create()).build();
    }

    public static RetrofitWrapper getInstance() {
        if (instance == null) {
            synchronized (RetrofitWrapper.class) {
                instance = new RetrofitWrapper();
            }
        }
        return instance;
    }

    public <T> T create(Class<T> service) {
        return retrofit.create(service);
    }

    public class Constant {
        public static final String BASE_URL = "http://japi.juhe.cn";
    }

}
