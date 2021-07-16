@Deprecated
Feature: Short term APSG with sectors end to end happy path journey

  Background:
    Given I have a "goods" "standard_international" licence
    And  I am on the VOL self-serve site
    And I select Short term ecmt permit on the select permit page
    And I select year on the select year page
    And I select short term ecmt period
    And I select any licence number for short term permit

  @EXTERNAL @OLCS-25904 @olcs-27581
  Scenario: Verify that ECMT short term 2020 application flow is correct and application is submitted successfully
    Then I should be on the overview page
    Then I complete the How will you use the permits section and click save and continue
    Then I complete Cabotage page section and click save and continue
    Then I complete Certificates required page section and click save and continue
    Then I complete Countries with limited permits section and click save and continue
    Then I complete Number of permits required section and click save and continue
    Then I complete Euro emissions standard page section and click save and continue
    Then I complete Annual trips abroad page section and click save and continue
    Then I complete Percentage of International journeys section and click save and continue
    Then I complete sectors page and click save and continue
    Then I click confirm and continue on the Check your answers page
    Then I click on Accept and continue on the Declaration page
    Then I click on Submit and Pay button on the Permit fee page and complete the payment
    Then I click on the Finish button on the Application submitted page