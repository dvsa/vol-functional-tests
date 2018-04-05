package org.dvsa.testing.framework.stepdefs.builders;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

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
    private String email;
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

    @JsonProperty("email")
    public String getEmail() {
        return email;
    }

    @JsonProperty("email")
    public void setEmail(String email) {
        this.email = email;
    }

    public ContactDetailsBuilder withEmailAddress(String emailAddress) {
        this.email = emailAddress;
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
        return "email:" + getEmail() + ",fao:" + getFao() + ",phone_primary:" + getPhoneNumber() + ",person:" + getPersonBuilder()
                + ",address:" + getAddress();
    }
}