package org.dvsa.testing.framework.Utils.API_Builders;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.junit.platform.commons.util.ToStringBuilder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "id",
        "consultant",
        "contact",
        "correspondenceAddress",
        "establishmentAddress",
        "consultant"
})
public class ApplicationAddressBuilder {

    @JsonProperty("id")
    private String id;
    @JsonProperty("consultant")
    private TransportConsultantBuilder consultant;
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
    public TransportConsultantBuilder getConsultant() {
        return consultant;
    }

    @JsonProperty("consultant")
    public void setConsultant(TransportConsultantBuilder consultant) {
        this.consultant = consultant;
    }

    public ApplicationAddressBuilder withConsultant(TransportConsultantBuilder consultant) {
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
        return new ToStringBuilder(ToStringStyle.JSON_STYLE)
        .append("id", getId())
                .append("contact", getContact())
                .append("consultant", getConsultant())
                .append("correspondenceAddress", getCorrespondenceAddress())
                .append("establishmentAddress", getEstablishmentAddress())
                .append("consultant", getConsultant())
                .toString();
    }
}