package org.dvsa.testing.framework.Utils.Generic;

import apiCalls.enums.TrafficArea;

public class ParseUtils {

    public static String parseMonth(String monthNumber) {
        return switch (monthNumber) {
            case "01" -> "January";
            case "02" -> "February";
            case "03" -> "March";
            case "04" -> "April";
            case "05" -> "May";
            case "06" -> "June";
            case "07" -> "July";
            case "08" -> "August";
            case "09" -> "September";
            case "10" -> "October";
            case "11" -> "November";
            case "12" -> "December";
                default -> throw new IllegalStateException("Month not found");
        };
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
