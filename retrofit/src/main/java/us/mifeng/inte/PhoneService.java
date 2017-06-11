package us.mifeng.inte;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;
import us.mifeng.beans.PhoneResult;

/**
 * Created by 黑夜之火 on 2017/4/3.
 */

public interface PhoneService {
    @GET("/apistore/mobilenumber/mobilenumber")
    Call<PhoneResult>getResult(@Header("apikey")String apikey, @Query("phone")String phone);
}
