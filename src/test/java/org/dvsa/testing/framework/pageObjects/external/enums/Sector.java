package org.dvsa.testing.framework.pageObjects.external.enums;

import activesupport.number.Int;

public enum Sector {

    CHEMICALS("Chemicals"),
    FOOD_PRODUCTS("Food products"),
    FURNITURE("Furniture"),
    METAL("Metal"),
    MAIL_AND_PARCELS("Mail and parcels"),
    RAW_MATERIALS("Raw materials & waste"),
    REFINED_FUELS("Refined fuels"),
    TRANSPORT_AND_MACHINERY("Transport & Machinery"),
    TEXTILES("Textiles"),
    UNREFINED_FUELS("Unrefined fuels"),
    WOOD("Wood"),
    OTHER_NON_METALLIC_MINERAL_PRODUCT("Other non-metallic mineral products"),
    NONE_OR_MORE_THAN_ONE_OF_THESE_SECTORS("None or more than one of these sectors");

    private String name;

    Sector(String name) {
        this.name = name;
    }

    public static org.dvsa.testing.framework.pageObjects.external.enums.Sector random(){
        return org.dvsa.testing.framework.pageObjects.external.enums.Sector.values()[Int.random(0, org.dvsa.testing.framework.pageObjects.external.enums.Sector.values().length - 1)];
    }

    @Override
    public String toString() {
        return name;
    }

}
