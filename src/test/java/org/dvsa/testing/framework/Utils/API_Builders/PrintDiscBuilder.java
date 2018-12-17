package org.dvsa.testing.framework.Utils.API_Builders;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.junit.platform.commons.util.ToStringBuilder;


@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "niFlag",
        "licenceType",
        "startNumber",
        "maxPages",
        "discSequence"
})

public class PrintDiscBuilder {

    @JsonProperty("niFlag")
    private String niFlag;
    @JsonProperty("licenceType")
    private String licenceType;
    @JsonProperty("startNumber")
    private String startNumber;
    @JsonProperty("maxPages")
    private String maxPages;
    @JsonProperty("discSequence")
    private String discSequence;

    @JsonProperty("niFlag")
    public String getNiFlag() {
        return niFlag;
    }

    @JsonProperty("niFlag")
    public void setNiFlag(String niFlag) {
        this.niFlag = niFlag;
    }

    public PrintDiscBuilder withNiFlag(String niFlag) {
        this.niFlag = niFlag;
        return this;
    }

    @JsonProperty("licenceType")
    public String getLicenceType() {
        return licenceType;
    }

    @JsonProperty("licenceType")
    public void setLicenceType(String licenceType) {
        this.licenceType = licenceType;
    }

    public PrintDiscBuilder withLicenceType(String licenceType) {
        this.licenceType = licenceType;
        return this;
    }

    @JsonProperty("startNumber")
    public String getStartNumber() {
        return startNumber;
    }

    @JsonProperty("startNumber")
    public void setStartNumber(String startNumber) {
        this.startNumber = startNumber;
    }

    public PrintDiscBuilder withStartNumber(String startNumber) {
        this.startNumber = startNumber;
        return this;
    }

    @JsonProperty("maxPages")
    public String getMaxPages() {
        return maxPages;
    }

    @JsonProperty("maxPages")
    public void setMaxPages(String maxPages) {
        this.maxPages = maxPages;
    }

    public PrintDiscBuilder withMaxPages(String maxPages) {
        this.maxPages = maxPages;
        return this;
    }

    @JsonProperty("discSequence")
    public String getDiscSequence() {
        return discSequence;
    }

    @JsonProperty("discSequence")
    public void setDiscSequence(String discSequence) {
        this.discSequence = discSequence;
    }

    public PrintDiscBuilder withDiscSequence(String discSequence) {
        this.discSequence = discSequence;
        return this;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("niFlag", niFlag)
                .append("licenceType", licenceType)
                .append("startNumber", startNumber)
                .append("maxPages", maxPages)
                .append("discSequence", discSequence).toString();
    }
}