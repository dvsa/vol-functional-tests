package org.dvsa.testing.framework.Utils.API_Builders;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.junit.platform.commons.util.ToStringBuilder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "id",
        "duePeriod",
        "caseworkerNotes"
})
public class GrantApplicationBuilder {

    @JsonProperty("id")
    private String id;
    @JsonProperty("duePeriod")
    private String duePeriod;
    @JsonProperty("caseworkerNotes")
    private String caseworkerNotes;
    @JsonProperty("reason")
    private String reason;
    @JsonProperty("grantAuthority")
    private String grantAuthority;

    @JsonProperty("id")
    public String getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
    }

    public GrantApplicationBuilder withId(String id) {
        this.id = id;
        return this;
    }

    @JsonProperty("duePeriod")
    public String getDuePeriod() {
        return duePeriod;
    }

    @JsonProperty("duePeriod")
    public void setDuePeriod(String duePeriod) {
        this.duePeriod = duePeriod;
    }

    public GrantApplicationBuilder withDuePeriod(String duePeriod) {
        this.duePeriod = duePeriod;
        return this;
    }

    @JsonProperty("caseworkerNotes")
    public String getCaseworkerNotes() {
        return caseworkerNotes;
    }

    @JsonProperty("caseworkerNotes")
    public void setCaseworkerNotes(String caseworkerNotes) {
        this.caseworkerNotes = caseworkerNotes;
    }

    public GrantApplicationBuilder withCaseworkerNotes(String caseworkerNotes) {
        this.caseworkerNotes = caseworkerNotes;
        return this;
    }

    @JsonProperty("reason")
    public String getReason() {
        return reason;
    }

    @JsonProperty("reason")
    public void setReason(String reason) {
        this.reason = reason;
    }

    public GrantApplicationBuilder withReason(String reason) {
        this.reason = reason;
        return this;
    }

    @JsonProperty("grantAuthority")
    public String getGrantAuthority() {
        return grantAuthority;
    }

    @JsonProperty("grantAuthority")
    public void setGrantAuthority(String grantAuthority) {
        this.grantAuthority = grantAuthority;
    }

    public GrantApplicationBuilder withAuthority(String grantAuthority){
        this.grantAuthority =grantAuthority;
        return this;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(ToStringStyle.JSON_STYLE)
                .append("id", getId())
                .append("duePeriod", getDuePeriod())
                .append("caseworkerNotes", getCaseworkerNotes())
                .append("reason", getReason())
                .append("grantAuthority", getGrantAuthority())
                .toString();
    }
}