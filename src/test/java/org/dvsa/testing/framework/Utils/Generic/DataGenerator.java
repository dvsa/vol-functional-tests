package org.dvsa.testing.framework.Utils.Generic;

import Injectors.World;
import activesupport.faker.FakerUtils;
import activesupport.number.Int;
import org.dvsa.testing.framework.pageObjects.BasePage;

public class DataGenerator extends BasePage {

    private World world;
    private String operatorUser;
    private String operatorUserEmail;
    private String operatorForeName;
    private String operatorFamilyName;

    public String getOperatorForeName() {
        return operatorForeName;
    }

    public void setOperatorForeName(String operatorForeName) {
        this.operatorForeName = operatorForeName;
    }

    public String getOperatorFamilyName() {
        return operatorFamilyName;
    }

    public void setOperatorFamilyName(String operatorFamilyName) {
        this.operatorFamilyName = operatorFamilyName;
    }

    public String getOperatorUser() {
        return operatorUser;
    }

    public void setOperatorUser(String operatorUser) {
        this.operatorUser = operatorUser;
    }

    public String getOperatorUserEmail() {
        return operatorUserEmail;
    }

    public void setOperatorUserEmail(String operatorUserEmail) {
        this.operatorUserEmail = operatorUserEmail;
    }

    public DataGenerator(World world) {
        this.world = world;
    }

    public void generateOperatorValues() {
        FakerUtils faker = new FakerUtils();
        setOperatorForeName(faker.generateFirstName());
        setOperatorFamilyName(faker.generateLastName());
        setOperatorUser(String.format("%s.%s%s",
                getOperatorForeName(),
                getOperatorFamilyName(), Int.random(1000, 9999))
        );
        setOperatorUserEmail(
                getOperatorUser().concat("@dvsaUser.com")
        );
    }

    public void generateAndAddOperatorUser() {
        world.selfServeNavigation.navigateToLogin(world.registerUser.getUserName(), world.registerUser.getEmailAddress());
        world.UIJourney.addUser();
    }
}