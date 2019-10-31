package org.dvsa.testing.framework.Utils.API_Builders;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "application",
        "noOfVehiclesRequired",
        "noOfTrailersRequired",
        "permission",
        "address"
})
public class OperatingCentreBuilder {

    @JsonProperty("application")
    private String application;
    @JsonProperty("noOfVehiclesRequired")
    private String noOfVehiclesRequired;
    @JsonProperty("noOfTrailersRequired")
    private String noOfTrailersRequired;
    @JsonProperty("permission")
    private String permission;
    @JsonProperty("address")
    private AddressBuilder address;

    @JsonProperty("application")
    public String getApplication() {
        return application;
    }

    @JsonProperty("application")
    public void setApplication(String application) {
        this.application = application;
    }

    public OperatingCentreBuilder withApplication(String application) {
        this.application = application;
        return this;
    }

    @JsonProperty("noOfVehiclesRequired")
    public String getNoOfVehiclesRequired() {
        return noOfVehiclesRequired;
    }

    @JsonProperty("noOfVehiclesRequired")
    public void setNoOfVehiclesRequired(String noOfVehiclesRequired) {
        this.noOfVehiclesRequired = noOfVehiclesRequired;
    }

    public OperatingCentreBuilder withNoOfVehiclesRequired(String noOfVehiclesRequired) {
        this.noOfVehiclesRequired = noOfVehiclesRequired;
        return this;
    }

    @JsonProperty("noOfTrailersRequired")
    public String getNoOfTrailersRequired() {
        return noOfTrailersRequired;
    }

    @JsonProperty("noOfTrailersRequired")
    public void setNoOfTrailersRequired(String noOfTrailersRequired) {
        this.noOfTrailersRequired = noOfTrailersRequired;
    }

    public OperatingCentreBuilder withNoOfTrailersRequired(String noOfTrailersRequired) {
        this.noOfTrailersRequired = noOfTrailersRequired;
        return this;
    }

    @JsonProperty("permission")
    public String getPermission() {
        return permission;
    }

    @JsonProperty("permission")
    public void setPermission(String permission) {
        this.permission = permission;
    }

    public OperatingCentreBuilder withPermission(String permission) {
        this.permission = permission;
        return this;
    }

    @JsonProperty("address")
    public AddressBuilder getAddress() {
        return address;
    }

    @JsonProperty("address")
    public void setAddress(AddressBuilder address) {
        this.address = address;
    }

    public OperatingCentreBuilder withAddress(AddressBuilder address) {
        this.address = address;
        return this;
    }


    @Override
    public String toString() {
        return  new ToStringBuilder(ToStringStyle.JSON_STYLE)
                .append("application", getApplication())
                .append("noOfVehiclesRequired", getNoOfVehiclesRequired())
                .append("noOfTrailersRequired", getNoOfTrailersRequired())
                .append("permission", getPermission())
                .append("address", getAddress()).toString();
    }
}