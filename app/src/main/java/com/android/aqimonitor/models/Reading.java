package com.android.aqimonitor.models;

/**
 * Created by trikh on 28-04-2017.
 */

public class Reading {
    private String name;
    private String value;
    private String unit;

    public Reading(String nameArg, String valueArg, String unitArgs) {
        name = nameArg;
        value = valueArg;
        unit = unitArgs;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public String getUnit() {
        return unit;
    }
}
