@EXTERNAL @ECMT @annual_ecmt_apgg_euro5_or_euro6
Feature: Self-serve dashboard

  Background:
    Given I have a "goods" "standard_international" licence

  @OLCS-21112 @olcs-27382
  Scenario: Displays all ECMT permit applications
    And I have completed an ECMT application
    And I view the application from ongoing permit application table
    Then my application should be under consideration

  @OLCS-25664
  Scenario: Selection of Not yet Submitted application navigates to application overview page
    And I have a partial completed ECMT application
    When I view the application from ongoing permit application table
    Then the overview page heading is displayed correctly

  @OLCS-25664 @OLCS-28275
  Scenario: Selection of awaiting fee ECMT application navigates to awaiting fee page
    And  I have an annual ECMT application in awaiting fee status
    When I view the application from ongoing permit application table
    Then the user is navigated to awaiting fee page