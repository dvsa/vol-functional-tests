@EXTERNAL @annual_ecmt_apgg_euro5_or_euro6 @eupa_regression
Feature: Application details page

  Background:
    Given I have a "goods" "standard_international" licence
    And I have completed an ECMT application
    And The application status on the self service dashboard goes to UNDER CONSIDERATION
    And I am viewing an application

  @EXTERNAL @ECMT @OLCS-25084 @E2EAnnualECMT @olcs-27382 @olcs-27581 @OLCS-28275
  Scenario: Application that's under consideration
    Then all the information should match that which was entered during the application process
    And the advisory text on ECMT under consideration page is displayed correctly
    And I click the back link
    Then I should be on the permits dashboard page with an ongoing application
    And I am viewing an application
    And I select return to permits dashboard hyperlink
    Then I should be on the permits dashboard page with an ongoing application
    And I am viewing an application
    And I select the withdraw application button
    And I should be navigated to the withdraw application page