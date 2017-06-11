package us.mifeng.rxjava.net;

import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by 黑夜之火 on 2017/3/26.
 */

public class HttpUtils {
    public String getWheather(String path,String city){
        //String cityStr = URLEncoder.encode(city);
        BufferedReader reader = null;
        String urlString = String.format(path, URLEncoder.encode(city));

        HttpURLConnection conn = null;
        try {
            URL url = new URL(urlString);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(50000);
            conn.connect();
            InputStream inputStream = conn.getInputStream();
            reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuffer buffer = new StringBuffer();
            String line = "";
            while(!TextUtils.isEmpty(line=reader.readLine())){
                buffer.append(line);
            }
            return buffer.toString();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (conn!=null){
                conn.disconnect();
                if (reader!=null){
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return null;
    }
    public String getString(String path){
        return "首先就是创建Observable，创建Observable有很多种方式，这里使用了Observable.create的方式；Observable.create()需要传入一个参数，这个参数其实是一个回调接口，在这个接口方法里我们处理开网络请求和解析xml的工作，并在最后通过onNext()、onCompleted()和onError()通知Subscriber（订阅者）; \n" +
                "然后就是调用Observable.subscribe()方法对Observable进行订阅。这里要注意，如果不调用Observable.subscribe()方法，刚才在Observable.create()处理的网络请求和解析xml的代码是不会执行的，这也就解释了本文开头所说的“如果没有观察者(即Subscribers)，Observables是不会发出任何事件的” \n" +
                "说了那么多，好像也没有开线程处理网络请求啊，这样不会报错吗？别急，认真看上面的代码，我还写了两个方法subscribeOn(Schedulers.newThread())和observeOn(AndroidSchedulers.mainThread())，没错，奥妙就在于此： \n" +
                "3.1 subscribeOn(Schedulers.newThread())表示开一个新线程处理Observable.create()方法里的逻辑，也就是处理网络请求和解析xml工作 \n" +
                "3.2 observeOn(AndroidSchedulers.mainThread())表示subscriber所运行的线程是在UI线程上，也就是更新控件的操作是在UI线程上 \n" +
                "3.3 如果这里只有subscribeOn()方法而没有observeOn()方法，那么Observable.create()和subscriber()都是运行在subscribeOn()所指定的线程中； \n" +
                "3.4 如果这里只有observeOn()方法而没有subscribeOn()方法，那么Observable.create()运行在主线程(UI线程)中，而subscriber()是运行在observeOn()所指定的线程中(本例的observeOn()恰好是指定主线程而已) ";

    }
}
