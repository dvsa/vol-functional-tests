package org.dvsa.testing.framework.pageObjects.enums;

public enum AdminOption {

    SCANNING("Scanning"),
    PRINTING("Printing"),
    PUBLIC_HOLIDAY("Public holidays"),
    CONTINUATIONS("Continuations"),
    YOUR_ACCOUNT("Your account"),
    SYSTEM_PARAMETERS("System parameters"),
    PERMITS("Permits"),
    TASK_ALLOCATION_RULE("Task allocation rules"),
    USER_MANAGEMENT("User management"),
    FINANCIAL_STANDING_RATES("Financial standing rates"),
    PUBLICATIONS("Publications"),
    PAYMENT_PROCESSING("Payment processing"),
    REPORTS("Reports"),
    FEATURE_TOGGLE("Feature toggle"),
    SYSTEM_MESSAGES("System messages"),
    DATA_RETENTION("Data retention");

    private String name;

    AdminOption(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }


}
