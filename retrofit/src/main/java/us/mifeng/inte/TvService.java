package us.mifeng.inte;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import us.mifeng.beans.TvInfo;

/**
 * Created by 黑夜之火 on 2017/4/3.
 */

public interface TvService {
    @GET("/tv/getChannel")
    Call<TvInfo>getTvInfoList(@Query("pId") String pId,
                              @Query("key") String key);
}
