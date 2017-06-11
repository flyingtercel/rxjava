package us.mifeng.rxjava;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import java.io.File;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func0;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.observables.GroupedObservable;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private int i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //rxTest();
        //rxTest1();
        //rxTest2();
        //rxTest3();
        //rxTest4();
        //rxText5();
        //rxText6();
        //rxTest7();
        //rxTest8();
        //rxTest9();
        //rxTest10();
        //rxBuffer();
        //groupBy();
        //cast();
        //scan();
        filter();
    }
    private void filter(){
        Observable.just(1,2,3,4,5,6).filter(new Func1<Integer, Boolean>() {
            @Override
            public Boolean call(Integer integer) {
                return integer<4;
            }
        }).subscribe(new Subscriber<Integer>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Integer integer) {
                log("filter"+integer);
            }
        });
    }
    private void cast(){
        Observable.just(1,2,3,4,5,6).cast(Integer.class).subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                log(integer+"");
            }
        });
    }
    private void scan(){
        Observable.just(1,2,3,4,5,6).scan(new Func2<Integer, Integer, Integer>() {
            @Override
            public Integer call(Integer integer, Integer integer2) {
                return integer+integer2;
            }
        }).subscribe(new Subscriber<Integer>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Integer integer) {
                log("==>"+integer);
            }
        });
    }
    private void groupBy(){
        Observable.interval(1,TimeUnit.SECONDS).take(10).groupBy(new Func1<Long, Long>() {
            @Override
            public Long call(Long aLong) {
                return aLong%3;
            }
        }).subscribe(new Action1<GroupedObservable<Long, Long>>() {
            @Override
            public void call(GroupedObservable<Long, Long> result) {
                result.subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        log("key"+result.getKey()+",value"+aLong);
                    }
                });
            }
        });
    }
    private Observable<File>listFiles(File f){
        if (f.isDirectory()){
            return Observable.from(f.listFiles()).flatMap(new Func1<File, Observable<File>>() {
                @Override
                public Observable<File> call(File file) {
                    return listFiles(file);
                }
            });
        }else{
            return Observable.just(f);
        }
    }
    public void click(View view){
        log(getApplicationContext().getExternalCacheDir().isDirectory()+"");
        Observable.just(getApplicationContext().getExternalCacheDir()).flatMap(new Func1<File, Observable<File>>() {
            @Override
            public Observable<File> call(File file) {
                return listFiles(file);
            }
        }).subscribe(new Action1<File>() {
                    @Override
                    public void call(File file) {
                        log(file.getAbsolutePath());
                    }
                });
    }

    private void rxBuffer() {
        final String[]mails = new String[]{"Here is an email!", "Another email!", "Yet another email!"};
        //每隔一秒就随机发布一个邮件
        Observable<String>endLessMail = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {
                    if (subscriber.isUnsubscribed()){
                        return;
                    }
                    Random random = new Random();
                    while(true){
                        String mail = mails[random.nextInt(mails.length)];
                        subscriber.onNext(mail);
                        Thread.sleep(1000);
                    }
                }catch (Exception e){
                    subscriber.onError(e);
                }
            }
        }).subscribeOn(Schedulers.io());
        //把上面产生的邮件缓存到列表中，并每隔3秒通知订阅着
        endLessMail.buffer(3,TimeUnit.SECONDS).subscribe(new Action1<List<String>>() {
            @Override
            public void call(List<String> strings) {
                log(String.format("You've got %d new messages!  Here they are!",strings.size()));
                for (int i=0;i<strings.size();i++){
                    log("**"+strings.get(i).toString());
                }
            }
        });
    }

    public void rxTest(){
        /**============正常流程======================*/
        /**-----消息源，被观察者-------------------*/
        rx.Observable<String> myObservable = Observable.create(new rx.Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext("hello word");
                subscriber.onCompleted();
            }
        });
        /**----------接收出，观察者----------------*/
        Subscriber<String>mySubscriber = new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String s) {
                //Log.i("tag","onNext--------------->"+s);
            }
        };
        /**-----------注册观察者---------------*/
        myObservable.subscribe(mySubscriber);
    }
    public void rxTest1(){
        /**-----------简化步骤------------------*/
        Observable.just(6).subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
               // Log.i("tag","Integer------------>"+integer);
            }
        });
        Observable.just("String args").subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                //Log.i("tag","s--------------->"+s);
            }
        });
    }
    public void rxTest2(){
        Observable.just("I an-->").map(new Func1<String, String>() {
            @Override
            public String call(String s) {
                return s+"map";
            }
        }).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                //Log.i("tag","maps----------------->"+s);
            }
        });
    }
    private void rxTest3() {
        Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                try {
                    if (!subscriber.isUnsubscribed()){
                        for(int i=0;i<5;i++){
                            subscriber.onNext(i);
                        }
                        subscriber.onCompleted();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).subscribe(new Subscriber<Integer>() {
            @Override
            public void onCompleted() {
                log("onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                log(e.getMessage());
            }

            @Override
            public void onNext(Integer integer) {
                log(integer+"");
            }
        });
    }
    private void rxTest4(){
        Integer[]items ={0,1,2,3,4,5};
        Observable myObservable = Observable.from(items);
        myObservable.subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer item) {
                System.out.println(item);
                log(item+"");
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
               // System.out.print("error encountered" + throwable.getMessage());
                log("error encountered" + throwable.getMessage());
            }
        }, new Action0() {
            @Override
            public void call() {
                //System.out.println("sequence complite");
                log("sequence complite");
            }
        });
    }
    private void rxText5(){
        Observable myObservable = Observable.just(1,2,3,4,5,6);
        myObservable.subscribe(new Subscriber<Integer>() {
            @Override
            public void onCompleted() {
                log("完成");
            }

            @Override
            public void onError(Throwable e) {
                log(e.getMessage());
            }

            @Override
            public void onNext(Integer o) {
                log("========x======"+o.toString());
            }
        });
    }
    private void rxText6(){
        i = 10;
        Observable justObservable = Observable.just(i);
        i = 120;
        Observable deferObservable = Observable.defer(new Func0<Observable<Integer>>() {
            @Override
            public Observable call() {
                return Observable.just(i);
            }
        });
        i = 1531;
        justObservable.subscribe(new Subscriber<Integer>() {
            @Override
            public void onCompleted() {
                log("just完成");
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Integer o) {
                log("j==========>ust"+o);
            }
        });
        deferObservable.subscribe(new Subscriber<Integer>() {
            @Override
            public void onCompleted() {
                log("defer完成");
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Integer o) {
                log("defer=========>"+o);
            }
        });
    }
    public void rxTest7(){
        Observable.timer(2,2, TimeUnit.SECONDS).subscribe(new Subscriber<Long>() {
            @Override
            public void onCompleted() {
                log("完成");
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Long aLong) {
                log("next"+aLong);
            }
        });
    }
    private void rxTest8(){
        Observable.range(3,10).repeat(3).subscribe(new Subscriber<Integer>() {
            @Override
            public void onCompleted() {
                log("完成");
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Integer integer) {
                log("next===>"+integer);
            }
        });
    }
    private void rxTest9(){
        Observable.just(1,2,3).repeatWhen(new Func1<Observable<? extends Void>, Observable<?>>() {
            @Override
            public Observable<?> call(final Observable<? extends Void> observable) {
                return observable.zipWith(Observable.range(1, 6), new Func2<Void, Integer, Integer>() {
                    @Override
                    public Integer call(Void aVoid, Integer integer) {
                        return integer;
                    }
                }).flatMap(new Func1<Integer, Observable<?>>() {
                    @Override
                    public Observable<?> call(Integer integer) {
                        log("delay repeat the===========>"+integer);
                        return Observable.timer(1,TimeUnit.SECONDS);
                    }
                });
            }
        }).subscribe(new Subscriber<Integer>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Integer integer) {

            }
        });
    }
    private void rxTest10(){
        Observable<Long>errorObservable = Observable.error(new Exception("this is error"));
        Observable<Long>observable1 = Observable.timer(0,1000,TimeUnit.SECONDS)
                .map(new Func1<Long, Long>() {
                    @Override
                    public Long call(Long aLong) {
                        return aLong*5;
                    }
                }).take(3).mergeWith(errorObservable.delay(3500,TimeUnit.SECONDS));
        Observable<Long>observable2 = Observable.timer(500,1000,TimeUnit.SECONDS)
                .map(new Func1<Long, Long>() {
                    @Override
                    public Long call(Long aLong) {
                        return aLong*5;
                    }
                }).take(5);
        Observable.mergeDelayError(observable1,observable2)
                .subscribe(new Subscriber<Long>() {
                    @Override
                    public void onCompleted() {
                        Log.i("tag","-----onCompleted----------->");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i("tag","-----onError----------->"+e.getMessage());
                    }

                    @Override
                    public void onNext(Long aLong) {
                        Log.i("tag","-----onNext----------->"+aLong);
                    }
                });
    }
    public void log(String str){
        Log.i("tag","-----------------------"+str);
    }
    private void study(){


    }
}
