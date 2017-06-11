package us.mifeng.rxjava.beans;

/**
 * Created by 黑夜之火 on 2017/3/26.
 */

public class Weather {
    String city;
    String data;
    String temperature;
    String direction;
    String power;
    String status;

    @Override
    public String toString() {
        return "Weather{" +
                "city='" + city + '\'' +
                ", data='" + data + '\'' +
                ", temperature='" + temperature + '\'' +
                ", direction='" + direction + '\'' +
                ", power='" + power + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
