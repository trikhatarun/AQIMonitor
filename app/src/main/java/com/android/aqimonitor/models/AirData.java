package com.android.aqimonitor.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by trikh on 05-04-2017.
 */

public class AirData {
    @SerializedName("status")
    private String status;
    @SerializedName("data")
    private Data data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Data {
        @SerializedName("aqi")
        private int aqi;
        @SerializedName("dominentpol")
        private String dominantPollutant;
        @SerializedName("city")
        private City city;
        @SerializedName("iaqi")
        private IAQI iaqi;

        public int getAqi() {
            return aqi;
        }

        public void setAqi(int aqi) {
            this.aqi = aqi;
        }

        public String getDominantPollutant() {
            return dominantPollutant;
        }

        public void setDominantPollutant(String dominantPollutant) {
            this.dominantPollutant = dominantPollutant;
        }

        public IAQI getIaqi() {
            return iaqi;
        }

        public void setIaqi(IAQI iaqi) {
            this.iaqi = iaqi;
        }

        public City getCity() {
            return city;
        }

        public void setCity(City city) {
            this.city = city;
        }
    }

    public class City {
        @SerializedName("name")
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public class IAQI {
        @SerializedName("co")
        private CO co;
        @SerializedName("h")
        private H h;
        @SerializedName("o3")
        private O3 o3;
        @SerializedName("t")
        private T t;
        @SerializedName("no2")
        private NO2 no2;
        @SerializedName("pm25")
        private PM25 pm25;

        public CO getCo() {
            return co;
        }

        public void setCo(CO co) {
            this.co = co;
        }

        public H getH() {
            return h;
        }

        public void setH(H h) {
            this.h = h;
        }

        public O3 getO3() {
            return o3;
        }

        public void setO3(O3 o3) {
            this.o3 = o3;
        }

        public T getT() {
            return t;
        }

        public void setT(T t) {
            this.t = t;
        }

        public NO2 getNo2() {
            return no2;
        }

        public void setNo2(NO2 no2) {
            this.no2 = no2;
        }

        public PM25 getPm25() {
            return pm25;
        }

        public void setPm25(PM25 pm25) {
            this.pm25 = pm25;
        }
    }

    public class NO2 {
        @SerializedName("v")
        private double v;

        public double getV() {
            return v;
        }

        public void setV(double v) {
            this.v = v;
        }
    }

    public class PM25 {
        @SerializedName("v")
        private double v;

        public double getV() {
            return v;
        }

        public void setV(double v) {
            this.v = v;
        }
    }

    public class T {
        @SerializedName("v")
        private double v;

        public double getV() {
            return v;
        }

        public void setV(double v) {
            this.v = v;
        }
    }

    public class O3 {
        @SerializedName("v")
        private double v;

        public double getV() {
            return v;
        }

        public void setV(double v) {
            this.v = v;
        }
    }

    public class H {
        @SerializedName("v")
        private double v;

        public double getV() {
            return v;
        }

        public void setV(double v) {
            this.v = v;
        }
    }

    public class CO {
        @SerializedName("v")
        private double v;

        public double getV() {
            return v;
        }

        public void setV(double v) {
            this.v = v;
        }
    }
}