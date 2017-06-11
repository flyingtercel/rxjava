package us.mifeng.retrofit;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import us.mifeng.beans.PhoneResult;
import us.mifeng.beans.TvInfo;
import us.mifeng.inte.PhoneService;
import us.mifeng.inte.TvService;
import us.mifeng.net.RetrofitWrapper;

public class MainActivity extends AppCompatActivity {
    private static final String BASE_URL = "http://apis.baidu.com";
    private static final String API_KEY="8e13586b86e4b7f3758ba3bd6c9c9135";
    private EditText phoneView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        phoneView = (EditText) findViewById(R.id.phoneView);
        Button btn = (Button) findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //query();
                retrofit();
            }
        });

    }

    private void query() {
        //创建retrofit对象
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())//解析方法
                .baseUrl(BASE_URL)//主机地址
                .build();
        //创建访问api的访问请求
        PhoneService service = retrofit.create(PhoneService.class);
        Call<PhoneResult>call = service.getResult(API_KEY,phoneView.getText().toString());
        //发送请求
        call.enqueue(new Callback<PhoneResult>() {
            @Override
            public void onResponse(Call<PhoneResult> call, Response<PhoneResult> response) {
                //处理结果
                if (response.isSuccess()){
                    PhoneResult result = response.body();
                    if (result!=null){
                        PhoneResult.RetDataEntity entity = result.getRetData();
                        Log.i("tag","-------------->"+entity);
                    }
                }
            }

            @Override
            public void onFailure(Call<PhoneResult> call, Throwable t) {
                Log.i("tag","----------------------->失败"+t.getMessage());
            }
        });
    }
    public void retrofit(){
        TvService tvService = RetrofitWrapper.getInstance().create(TvService.class);
        Call<TvInfo> call = tvService.getTvInfoList(
                "1", "8a7204eb63ba0bab009b338025f42df2"
        );
        call.enqueue(
                new Callback<TvInfo>() {
                    @Override
                    public void onResponse(Call<TvInfo> call, Response<TvInfo> response) {
                        TvInfo tv = response.body();
                        //这里显示了列表中的第一条的内容，所以get(0)
                        Toast.makeText(
                                MainActivity.this,
                                tv.getResult().get(0).getChannelName(),
                                Toast.LENGTH_LONG
                        ).show();
                        Log.i("tag","-------------->"+response.body().toString());
                    }

                    @Override
                    public void onFailure(Call<TvInfo> call, Throwable t) {
                        Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                        Log.i("tag","------失败-------->"+t.getMessage());
                    }
                }
        );

    }
}
