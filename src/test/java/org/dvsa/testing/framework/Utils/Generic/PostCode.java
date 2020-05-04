package org.dvsa.testing.framework.Utils.Generic;

import Injectors.World;
import activesupport.number.Int;
import enums.TrafficArea;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class PostCode {

    private World world;
    private String area;

    public PostCode(World world){
        this.world = world;
    }

    public String getArea() {
        return area;
    }


    public static String getPostCode(@NotNull TrafficArea trafficArea) {
        String postCode;
        switch (trafficArea) {
            case B:
                postCode = "BD162UA";
                break;
            case C:
                postCode = "M446TL";
                break;
            case D:
                postCode = "B440TA";
                break;
            case F:
                postCode = "IP138ES";
                break;
            case G:
                postCode = "CF116EE";
                break;
            case H:
                postCode = "OX11BY";
                break;
            case K:
                postCode = "E72EW";
                break;
            case M:
                postCode = "EH139DY";
                break;
            case N:
                postCode = "BT28HQ";
                break;
            default:
                postCode = "NG23HX";
        }
        return postCode;
    }

    public static String getRandomRealNottinghamPostcode() {
        String postcodeFile = System.getProperty("user.dir").concat("/src/test/resources/testResources/nottinghamPostcodes.csv");
        String file = null;
        try {
            file = new String(Files.readAllBytes(Paths.get(postcodeFile)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        String[] postcodes = file.split(",\n");
        return postcodes[Int.random(0, postcodes.length - 1)];
    }
}