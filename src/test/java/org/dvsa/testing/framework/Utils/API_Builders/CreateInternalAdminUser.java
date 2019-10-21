package org.dvsa.testing.framework.Utils.API_Builders;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "contactDetails",
        "userType",
        "osType",
        "loginId",
        "team",
        "roles",
        "id",
        "version"

})
public class CreateInternalAdminUser {

    @JsonProperty("contactDetails")
    private ContactDetailsBuilder contactDetails;
    @JsonProperty("userType")
    private String userType;
    @JsonProperty("osType")
    private String osType;
    @JsonProperty("loginId")
    private String loginId;
    @JsonProperty("team")
    private String team;
    @JsonProperty("roles")
    private List<String> roles = null;
    @JsonProperty("id")
    private String id;
    @JsonProperty("version")
    private String version;

    @JsonProperty("contactDetails")
    public ContactDetailsBuilder getContactDetails() {
        return contactDetails;
    }

    @JsonProperty("contactDetails")
    public void setContactDetails(ContactDetailsBuilder contactDetails) {
        this.contactDetails = contactDetails;
    }

    public CreateInternalAdminUser withContactDetails(ContactDetailsBuilder contactDetails) {
        this.contactDetails = contactDetails;
        return this;
    }

    @JsonProperty("userType")
    public String getUserType() {
        return userType;
    }

    @JsonProperty("userType")
    public void setUserType(String userType) {
        this.userType = userType;
    }

    public CreateInternalAdminUser withUserType(String userType) {
        this.userType = userType;
        return this;
    }

    @JsonProperty("osType")
    public String getOsType(){
        return osType;
    }

    @JsonProperty("osType")
    public void setOsType(String osType){
        this.osType = osType;
    }

    public CreateInternalAdminUser withOSType(String osType){
        this.osType = osType;
        return this;
    }

    @JsonProperty("loginId")
    public String getLoginId() {
        return loginId;
    }

    @JsonProperty("loginId")
    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public CreateInternalAdminUser withLoginId(String loginId) {
        this.loginId = loginId;
        return this;
    }

    @JsonProperty("team")
    public String getTeam() {
        return team;
    }

    @JsonProperty("team")
    public void setTeam(String team) {
        this.team = team;
    }

    public CreateInternalAdminUser withTeam(String team) {
        this.team = team;
        return this;
    }

    @JsonProperty("roles")
    public List<String> getRoles() {
        return roles;
    }

    @JsonProperty("roles")
    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public CreateInternalAdminUser withRoles(List<String> roles) {
        this.roles = roles;
        return this;
    }

    @JsonProperty("id")
    public String getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
    }

    public CreateInternalAdminUser withId(String id) {
        this.id = id;
        return this;
    }

    @JsonProperty("version")
    public String getVersion() {
        return version;
    }

    @JsonProperty("version")
    public void setVersion(String version) {
        this.version = version;
    }

    public CreateInternalAdminUser withVersion(String version) {
        this.version = version;
        return this;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(ToStringStyle.JSON_STYLE)
        .append("contactDetails", getContactDetails())
                .append("userType", getUserType())
                .append("osType",getOsType())
                .append("loginId", getLoginId())
                .append("team", getTeam())
                .append("roles", getRoles())
                .append("id",getId())
                .append("version",getVersion()).toString();
    }
}