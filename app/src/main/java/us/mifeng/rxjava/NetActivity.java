package us.mifeng.rxjava;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import us.mifeng.rxjava.beans.Weather;
import us.mifeng.rxjava.beans.XMLWeather;
import us.mifeng.rxjava.net.HttpUtils;

import static us.mifeng.rxjava.R.id.weather;

public class NetActivity extends AppCompatActivity implements View.OnClickListener{
    /**天气预报的API地址*/
    private static final String WEATHRE_API_URL="http://php.weather.sina.com.cn/xml.php?city=%s&password=DJOYnieT8234jlsK&day=0";
    private static final String path = "http://www.weather.com.cn/data/cityinfo/101010100.html";
    private EditText cityET;
    private TextView queryET;
    private TextView weatherET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_net);
        init();


    }

    private void init() {
        cityET = (EditText) findViewById(R.id.searchText);
        queryET = (TextView) findViewById(R.id.query);
        weatherET = (TextView) findViewById(weather);
        queryET.setOnClickListener(this);
        weatherET.setOnClickListener(this);
    }
    private void observableAsNormal(final String city){
        /**
         * Observable：可观察这对象创建，
         * OnSubscribe:在订阅的时候，预约数据处理，在这儿就是预约天气接口数据的请求与数据解析
         *通过onNext(),onError(),onComplete()方法通知预约着。
         * Observable.subscribe()方法对Observable进行了订阅。
         *subscribeOn()指明了在订阅的时候，数据在那个线程中运行
         * observeOn()指明了观察者在那个线程中运行。
         * */
        Subscription subscribe = Observable.create(new Observable.OnSubscribe<Weather>() {

            @Override
            public void call(Subscriber<? super Weather> subscriber) {
                if (subscriber.isUnsubscribed()) {
                    return;
                }
                try {
                    String weatherXml = new HttpUtils().getWheather(WEATHRE_API_URL, city);
                    Weather weather = new XMLWeather().parseWeather(weatherXml);
                    subscriber.onNext(weather);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        }).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Weather>() {
                    @Override
                    public void onCompleted() {
                        Log.i("tag","---------------onCompleted-------------");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i("tag","---------e-----------"+e.getMessage());
                    }

                    @Override
                    public void onNext(Weather weather) {
                        if (weather != null) {
                            weatherET.setText(weather.toString());
                        }
                        Log.i("tag","-------weather为空-------------"+weather);
                    }
                });
    }
    private void observableAsLambda(String city){
        Subscription subscribe = Observable.create(subscriber -> {
            try {
                if (subscriber.isUnsubscribed()) return;
                String weatherXml = new HttpUtils().getWheather(WEATHRE_API_URL, city);
                Weather weather = new XMLWeather().parseWeather(weatherXml);
                subscriber.onNext(weather);
                subscriber.onCompleted();
            } catch (Exception e) {
                subscriber.onError(e);
            }
        }).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(weather -> {
                    if (weather != null) {
                        weatherET.setText(weather.toString());
                    }
                }, e -> {
                    Toast.makeText(NetActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                });
    }
    public void fun(){
        Observable<String>observable = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                if (subscriber.isUnsubscribed()){
                    return;
                }
                try {
                    String ss = new HttpUtils().getString(path);
                    subscriber.onNext(ss);
                    subscriber.onCompleted();
                }catch (Exception e){
                    subscriber.onError(e);
                }
            }
        });
        observable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(ss->{
                    if (ss!=null){
                        weatherET.setText(ss);
                    }
                },e->{
                    Toast.makeText(NetActivity.this,"------"+e.getMessage(),Toast.LENGTH_LONG).show();
                });
    }

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.query){
            weatherET.setText("");
            String city = cityET.getText().toString();
            if (TextUtils.isEmpty(city)){
                Toast.makeText(NetActivity.this,"城市名不能为空",Toast.LENGTH_LONG).show();
                return;
            }
            //observableAsNormal(city);
            //observableAsLambda(city);
            fun();
        }
    }
}
