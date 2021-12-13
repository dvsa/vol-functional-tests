package org.dvsa.testing.framework.Utils.Generic;

import Injectors.World;
import activesupport.faker.FakerUtils;
import activesupport.number.Int;
import apiCalls.enums.TrafficArea;
import org.dvsa.testing.framework.pageObjects.BasePage;

import java.util.HashMap;

public class DataGenerator extends BasePage {

    private World world;
    private String operatorUser;
    private String operatorUserEmail;
    private String operatorForeName;
    private String operatorFamilyName;
    private String operatorAddressLine1;
    private String operatorAddressLine2;
    private String operatorTown;
    private String operatorPostCode;



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

    public String getOperatorAddressLine1() {
        return operatorAddressLine1;
    }

    public void setOperatorAddressLine1(String operatorAddressLine1) {
        this.operatorAddressLine1 = operatorAddressLine1;
    }

    public String getOperatorAddressLine2() {
        return operatorAddressLine2;
    }

    public void setOperatorAddressLine2(String operatorAddressLine2) {
        this.operatorAddressLine2 = operatorAddressLine2;
    }

    public String getOperatorTown() {
        return operatorTown;
    }

    public void setOperatorTown(String operatorTown) {
        this.operatorTown = operatorTown;
    }

    public String getOperatorPostCode() {
        return operatorPostCode;
    }

    public void setOperatorPostCode(String operatorPostCode) {
        this.operatorPostCode = operatorPostCode;
    }

    public DataGenerator(World world) {
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
        setOperatorAddressLine1(faker.generateAddress().get("addressLine1"));
        setOperatorAddressLine2(faker.generateAddress().get("addressLine2"));
        setOperatorTown(faker.generateAddress().get("town"));
        setOperatorPostCode(TrafficArea.getPostCode(TrafficArea.MIDLANDS));
        this.world = world;
    }

    public void generateAndAddOperatorUser() {
        world.selfServeNavigation.navigateToLogin(world.registerUser.getUserName(), world.registerUser.getEmailAddress());
        world.UIJourney.addUser();
    }
}