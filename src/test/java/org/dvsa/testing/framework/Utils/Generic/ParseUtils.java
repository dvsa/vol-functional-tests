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
        String trafficAreaString;
        switch (trafficArea) {
            case NORTH_EAST:
                trafficAreaString = "North East of England";
                break;
            case NORTH_WEST:
                trafficAreaString = "North West of England";
                break;
            case MIDLANDS:
                trafficAreaString = "West Midlands";
                break;
            case EAST:
                trafficAreaString = "East of England";
                break;
            case WALES:
                trafficAreaString = "Wales";
                break;
            case WEST:
                trafficAreaString = "West of England";
                break;
            case LONDON:
                trafficAreaString = "London and the South East of England";
                break;
            case SCOTLAND:
                trafficAreaString = "Scotland";
                break;
            case NORTHERN_IRELAND:
                trafficAreaString = "Northern Ireland";
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + trafficArea);
        }
        return trafficAreaString;
    }
}
