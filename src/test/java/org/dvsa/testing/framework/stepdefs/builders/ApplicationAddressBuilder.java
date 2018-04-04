package org.dvsa.testing.framework.stepdefs.builders;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "id",
        "consultant",
        "contact",
        "correspondenceAddress"
})
public class ApplicationAddressBuilder {

    @JsonProperty("id")
    private String id;
    @JsonProperty("consultant")
    private String consultant;
    @JsonProperty("contact")
    private ContactDetailsBuilder contact;
    @JsonProperty("correspondenceAddress")
    private AddressBuilder correspondenceAddress;
    @JsonProperty("establishmentAddress")
    private AddressBuilder establishmentAddress;

    @JsonProperty("id")
    public String getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
    }

    public ApplicationAddressBuilder withId(String id) {
        this.id = id;
        return this;
    }

    @JsonProperty("consultant")
    public String getConsultant() {
        return consultant;
    }

    @JsonProperty("consultant")
    public void setConsultant(String consultant) {
        this.consultant = consultant;
    }

    public ApplicationAddressBuilder withConsultant(String consultant) {
        this.consultant = consultant;
        return this;
    }


    @JsonProperty("contact")
    public ContactDetailsBuilder getContact() {
        return contact;
    }

    @JsonProperty("contact")
    public void setContact(ContactDetailsBuilder contact) {
        this.contact = contact;
    }

    public ApplicationAddressBuilder withContact(ContactDetailsBuilder contact) {
        this.contact = contact;
        return this;
    }

    @JsonProperty("correspondenceAddress")
    public AddressBuilder getCorrespondenceAddress() {
        return correspondenceAddress;
    }

    @JsonProperty("correspondenceAddress")
    public void setCorrespondenceAddress(AddressBuilder correspondenceAddress) {
        this.correspondenceAddress = correspondenceAddress;
    }

    public ApplicationAddressBuilder withCorrespondenceAddress(AddressBuilder correspondenceAddress) {
        this.correspondenceAddress = correspondenceAddress;
        return this;
    }

    @JsonProperty("establishmentAddress")
    public AddressBuilder getEstablishmentAddress() {
        return establishmentAddress;
    }

    @JsonProperty("establishmentAddress")
    public void setEstablishmentAddress(AddressBuilder establishmentAddress) {
        this.establishmentAddress = establishmentAddress;
    }

    public ApplicationAddressBuilder withEstablishmentAddress(AddressBuilder establishmentAddress) {
        this.establishmentAddress = establishmentAddress;
        return this;
    }

    @Override
    public String toString() {
        return "id:" + getId() + ",contact:" + getContact() + ",consultant:" + getConsultant()
                + ",correspondenceAddress:" + getCorrespondenceAddress() + ",establishmentAddress:" + getEstablishmentAddress();
    }
}