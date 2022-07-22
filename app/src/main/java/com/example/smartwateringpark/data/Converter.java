package com.example.smartwateringpark.data;

import java.util.Date;

public class Converter {
    public static Date toDate(Long dateLong){
        return dateLong == null ? null: new Date(dateLong);
    }
    public static Long fromDate(Date date){
        return date == null ? null : date.getTime();
    }
}
