package org.dvsa.testing.framework.Utils.Generic;

import apiCalls.enums.TrafficArea;

public class ParseUtils {

    public static String parseMonth(String monthNumber) {
        String monthName;
        switch (monthNumber) {
            case "01":
                monthName = "January";
                break;
            case "02":
                monthName = "February";
                break;
            case "03":
                monthName = "March";
                break;
            case "04":
                monthName = "April";
                break;
            case "05":
                monthName = "May";
                break;
            case "06":
                monthName = "June";
                break;
            case "07":
                monthName = "July";
                break;
            case "08":
                monthName = "August";
                break;
            case "09":
                monthName = "September";
                break;
            case "10":
                monthName = "October";
                break;
            case "11":
                monthName = "November";
                break;
            case "12":
                monthName = "December";
                break;
            default:
                monthName = "Date Not Found";
        }
        return monthName;
    }

    public static String parseTrafficArea(TrafficArea trafficArea) {
        return switch (trafficArea) {
            case NORTH_EAST -> "North East Of England";
            case NORTH_WEST -> "North West Of England";
            case MIDLANDS -> "West Midlands";
            case EAST  -> "East of England";
            case WALES -> "Wales";
            case WEST -> "West of England";
            case LONDON -> "London";
            case SCOTLAND -> "Scotland";
            case NORTHERN_IRELAND -> "Northern Ireland";
        };
    }
}
