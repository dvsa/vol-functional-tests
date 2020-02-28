package org.dvsa.testing.framework.Utils.API_Builders;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
/*
 I CHANGED EMAILADDRESS TO EMAIL ON A COUPLE BECAUSE THEY DIDN'T MATCH AND THE JSON WAS GENERATING EXTRA DUPLICATE SECTIONS. IS THIS OKAY OR WAS THERE A REASON WHY WE NEEDED THE DUPLICATE UNDER A DIFFERENT KEY?!!
*/
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "email",
        "person",
        "fao",
        "phone_primary",
        "address"
})
public class ContactDetailsBuilder {

    @JsonProperty("email")
    private String emailAddress;
    @JsonProperty("person")
    private PersonBuilder personBuilder;
    @JsonProperty("fao")
    private String fao;
    @JsonProperty("phone_primary")
    private String phoneNumber;
    @JsonProperty("address")
    private AddressBuilder address;

    @JsonProperty("phone_primary")
    public String getPhoneNumber() {
        return phoneNumber;
    }

    @JsonProperty("phoneNumber")
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public ContactDetailsBuilder withPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    @JsonProperty("emailAddress")
    public String getEmailAddress() {
        return emailAddress;
    }

    @JsonProperty("emailAddress")
    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public ContactDetailsBuilder withEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
        return this;
    }

    @JsonProperty("person")
    public PersonBuilder getPersonBuilder() {
        return personBuilder;
    }

    @JsonProperty("person")
    public void setPersonBuilder(PersonBuilder personBuilder) {
        this.personBuilder = personBuilder;
    }

    public ContactDetailsBuilder withPerson(PersonBuilder personBuilder) {
        this.personBuilder = personBuilder;
        return this;
    }

    @JsonProperty("fao")
    public String getFao() {
        return fao;
    }

    @JsonProperty("fao")
    public void setFao(String fao) {
        this.fao = fao;
    }

    public ContactDetailsBuilder withFao(String fao) {
        this.fao = fao;
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

    public ContactDetailsBuilder withAddress(AddressBuilder address) {
        this.address = address;
        return this;
    }

    @Override
    public String toString() {
        return "email:" + getEmailAddress() + ",fao:" + getFao() + ",phone_primary:" + getPhoneNumber() + ",person:" + getPersonBuilder()
                + ",address:" + getAddress();
    }
}