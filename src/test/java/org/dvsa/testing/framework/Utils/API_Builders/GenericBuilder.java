package org.dvsa.testing.framework.Utils.API_Builders;

import activesupport.string.Str;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "id",
        "version",
        "licenceType"
})
public class GenericBuilder {

    @JsonProperty("id")
    private String id;
    @JsonProperty("version")
    private Integer version;
    @JsonProperty("licenceType")
    private String licenceType;
    @JsonProperty("Address")
    private AddressBuilder address;

    @JsonProperty("id")
    public String getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
    }

    public GenericBuilder withId(String id) {
        this.id = id;
        return this;
    }

    @JsonProperty("version")
    public Integer getVersion() {
        return version;
    }

    @JsonProperty("version")
    public void setVersion(Integer version) {
        this.version = version;
    }

    public GenericBuilder withVersion(Integer version) {
        this.version = version;
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

    public GenericBuilder withLicenceType(String licenceType) {
        this.licenceType = licenceType;
        return this;
    }

    @JsonProperty("Address")
    public AddressBuilder getAddress() {
        return address;
    }

    @JsonProperty("Address")
    public void setAddress(AddressBuilder address) {
        this.address = address;
    }

    public GenericBuilder withAddress(AddressBuilder address) {
        this.address = address;
        return this;
    }

    @Override
    public String toString() {
        return "id" + getId() + ",version:" + getVersion() + ",licenceType:" + getLicenceType() + ",registeredAddress:" + getAddress();
    }
}