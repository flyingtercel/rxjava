package us.mifeng.rxjava;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class WelcomeActivity extends AppCompatActivity {
    private int num = 5;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        imageView = (ImageView) findViewById(R.id.imageview);
         skip();
        //showAdvertisment();
    }

    private void showAdvertisment() {
        Observable loadBitmapFromLocal = Observable.create(new Observable.OnSubscribe<String>() {

            @Override
            public void call(Subscriber o) {
                o.onNext("");
            }
        });
        Observable loadBitmapFromNet = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber o) {
                o.onNext("");
            }
        });
        Observable.mergeDelayError(loadBitmapFromLocal.subscribeOn(Schedulers.io()),
                loadBitmapFromNet.subscribeOn(Schedulers.newThread()),
                Observable.timer(3,TimeUnit.SECONDS).map(new Func1() {
                    @Override
                    public Object call(Object o) {
                        return null;
                    }
                }).sample(3,TimeUnit.SECONDS,AndroidSchedulers.mainThread())).flatMap(new Func1<Bitmap,Object>() {
            @Override
            public Object call(Bitmap bitmap) {
                if (bitmap==null){
                    return  Observable.empty();
                }else{
                    imageView.setImageBitmap(bitmap);
                    return Observable.timer(3,TimeUnit.SECONDS,AndroidSchedulers.mainThread());
                }
            }
        }).subscribe(new Action1() {
            @Override
            public void call(Object o) {
                startActivity(new Intent(WelcomeActivity.this,MainActivity.class));

            }
        });

    }

    private void skip() {
        Observable.timer(1,TimeUnit.SECONDS, AndroidSchedulers.mainThread()).map(new Func1<Long, Object>() {
            @Override
            public Object call(Long aLong) {
               // Log.i("tag","----------------->"+(num-aLong));
                startActivity(new Intent(WelcomeActivity.this,MainActivity.class));
                finish();
                return null;
            }
        }).subscribe();
    }

}
