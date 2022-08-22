package org.dvsa.testing.framework.pageObjects.type;

public class Fee {
    private String year;
    private String validityPeriod;
    private int feePerPermit;
    private int numberOfPermits;
    private int totalFee;


    public Fee(String year, String validityPeriod, int feePerPermit, int numberOfPermits,
               int totalFee) {

        this.year = year;
        this.validityPeriod = validityPeriod;
        this.feePerPermit = feePerPermit;
        this.numberOfPermits = numberOfPermits;
        this.totalFee = totalFee;
    }

}