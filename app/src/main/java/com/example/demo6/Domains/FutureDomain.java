package com.example.demo6.Domains;

public class FutureDomain {
    private String day;
    private String picPath;
    private  String status;
    private int highTemp;
    private int lowTemp;

    public FutureDomain(String day, String picPath, String status, int highTemp, int lowTemp) {
        this.day = day;
        this.picPath = picPath;
        this.status = status;
        this.highTemp = highTemp;
        this.lowTemp = lowTemp;
    }

    public String getDay() {
        return day;
    }

    public String getPicPath() {
        return picPath;
    }

    public String getStatus() {
        return status;
    }

    public int getHighTemp() {
        return highTemp;
    }

    public int getLowTemp() {
        return lowTemp;
    }
}
