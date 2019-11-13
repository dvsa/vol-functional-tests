package org.dvsa.testing.framework.stepdefs;

import Injectors.World;
import activesupport.config.Configuration;
import activesupport.database.DBUnit;
import activesupport.jenkins.Jenkins;
import activesupport.jenkins.JenkinsParameterKey;
import activesupport.system.Properties;
import com.typesafe.config.Config;
import cucumber.api.java8.En;
import io.restassured.internal.NoParameterValue;
import org.dvsa.testing.lib.pages.BasePage;
import org.dvsa.testing.lib.pages.enums.SelectorType;
import org.dvsa.testing.lib.url.utils.EnvironmentType;

import java.sql.ResultSet;
import java.util.HashMap;

import static junit.framework.TestCase.assertEquals;
import static org.dvsa.testing.framework.stepdefs.RemoveTM.alertHeaderValue;

public class GenerateLastTMLetter extends BasePage implements En {

    public GenerateLastTMLetter(World world) {

        EnvironmentType env = EnvironmentType.getEnum(Properties.get("env", true));


        Given("^i have a valid \"([^\"]*)\" \"([^\"]*)\" licence$", (String operatorType, String licenceType) -> {
            world.UIJourneySteps.createLicence(world, operatorType, licenceType);
        });
        Then("^a flag should be set in the DB$", () -> {
            Config config = new Configuration(env.toString()).getConfig();
            if (config.getString("dbUsername").isEmpty() || config.getString("dbPassword").isEmpty()){
                throw new Exception("No values for 'dbUsername' and 'dbPassword' from the config file.")
            }
            Properties.set("dbUsername",config.getString("dbUsername"));
            Properties.set("dbPassword",config.getString("dbPassword"));
            ResultSet resultSet = DBUnit.checkResult(String.format("SELECT opt_out_tm_letter FROM OLCS_RDS_OLCSDB.licence\n" +
                    "WHERE lic_no='%s';", world.createLicence.getLicenceNumber()));
            if (resultSet.next()) {
                int columnValue = Integer.parseInt(resultSet.getString("opt_out_tm_letter"));
                assertEquals(0, columnValue);
            }
        });
        Given("^the licence status is \"([^\"]*)\"$", (String arg0) -> {
            world.updateLicence.updateLicenceStatus(world.createLicence.getLicenceId(), arg0);
        });
        And("^the user confirms they want to send letter$", () -> {
            waitForTextToBePresent(alertHeaderValue);
            findSelectAllRadioButtonsByValue("Y");
            click("//*[@id='form-actions[submit]']", SelectorType.XPATH);
        });
        And("^the batch job has run$", () -> {
            HashMap<String, String> jenkinsParams = new HashMap<>();
            jenkinsParams.put(JenkinsParameterKey.NODE.toString(), String.format("%s&&api&&olcs", Properties.get("env", true)));
            jenkinsParams.put(JenkinsParameterKey.COMMAND.toString(), "last-tm-letter");

            Jenkins.trigger(Jenkins.Job.BATCH_RUN_CLI, jenkinsParams);
        });
        And("^i navigate to the review and declarations page and submit the application$", () -> {
            clickByLinkText("GOV.UK");
            clickByLinkText(world.createLicence.getApplicationNumber());
            clickByLinkText("Review and declarations");
            waitAndClick("//*[@id='label-declarationConfirmation']", SelectorType.XPATH);
            click("//*[@id='submit']",SelectorType.XPATH);
        });
    }
}