package org.dvsa.testing.framework.Utils.API_Builders;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.junit.platform.commons.util.ToStringBuilder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder ({
        "add-transport-consultant",
        "address",
        "transportConsultantName",
        "writtenPermissionToEngage",
        "contact"
        })

public class TransportConsultantBuilder {

    @JsonProperty("add-transport-consultant")
    private String confirmation = "Y";
    @JsonProperty("address")
    private AddressBuilder address;
    @JsonProperty("transportConsultantName")
    private String name;
    @JsonProperty("writtenPermissionToEngage")
    private String writtenPermissionToEngage;
    @JsonProperty("contact")
    private ContactDetailsBuilder contact;

    @JsonProperty("add-transport-consultant")
    public String getConfirmation(){
        return this.confirmation;
    }

    @JsonProperty("add-transport-consultant")
    public void setConfirmation(String confirmation){
        this.confirmation = confirmation;
    }

    public TransportConsultantBuilder withConfirmation(String confirmation){
        this.confirmation = confirmation;
        return this;
    }

    @JsonProperty("address")
    public AddressBuilder getAddress() {
        return this.address;
    }

    @JsonProperty("address")
    public void setAddress(AddressBuilder address){
        this.address = address;
    }

    public TransportConsultantBuilder withAddress(AddressBuilder address){
        this.address = address;
        return this;
    }

    @JsonProperty("transportConsultantName")
    public String getName() {
        return this.name;
    }

    @JsonProperty("transportConsultantName")
    public void setName(String name){
        this.name = name;
    }

    public TransportConsultantBuilder withName(String name) {
        this.name = name;
        return this;
    }

    @JsonProperty("writtenPermissionToEngage")
    public String getWrittenPermissionToEngage(){
        return this.writtenPermissionToEngage;
    }

    @JsonProperty("writtenPermissionToEngage")
    public void setWrittenPermissionToEngage(String writtenPermissionToEngage){
        this.writtenPermissionToEngage = writtenPermissionToEngage;
    }

    public TransportConsultantBuilder withWrittenPermissionToEngage(String writtenPermissionToEngage){
        this.writtenPermissionToEngage = writtenPermissionToEngage;
        return this;
    }

    @JsonProperty("contact")
    public ContactDetailsBuilder getContact(){
        return this.contact;
    }

    @JsonProperty("contact")
    public void setContact(ContactDetailsBuilder contact){
        this.contact = contact;
    }

    public TransportConsultantBuilder withContact(ContactDetailsBuilder contact){
        this.contact = contact;
        return this;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(ToStringStyle.JSON_STYLE)
                .append( "add-transport-consultant", getConfirmation())
                .append("address", getAddress())
                .append("transportConsultantName", getName())
                .append("writtenPermissionToEngage", getWrittenPermissionToEngage())
                .append("contact", getContact())
                .toString();
    }
}