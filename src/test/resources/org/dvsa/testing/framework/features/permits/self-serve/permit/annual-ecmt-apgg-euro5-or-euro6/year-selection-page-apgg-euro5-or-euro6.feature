@EXTERNAL @ECMT @annual_ecmt_apgg_euro5_or_euro6
Feature: Annual Ecmt Year Selection Page

  Background:
    Given I have a "goods" "standard_international" licence
    And I am on the Year Selection Page

  @OLCS-24972
  Scenario: Application Back button
    When I click the back link
    Then the user is navigated to the permit type page

  @OLCS-24972
  Scenario: Page Heading and advisory messages are displayed correctly
    Then the page heading on Annual Ecmt Year selection page is displayed correctly
    And the validity error message is displayed
    When I confirm  the year selection
    Then the user is navigated to licence selection page