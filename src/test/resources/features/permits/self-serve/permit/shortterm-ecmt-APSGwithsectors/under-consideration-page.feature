@Deprecated
Feature: Short term ECMT APSG with sectors under consideration page

  Background:
    Given I have a "goods" "standard_international" licence
    And  I am on the VOL self-serve site
    And  I am on Short term under consideration page

  @OLCS-25670
  Scenario: Under Consideration page details are displayed correctly
    Then the user is on the under consideration page
    And the table of contents in the short term  under consideration page are displayed correctly
    And the warning message is displayed correctly
    When I select withdraw application button
    Then I am taken to the Withdraw Application page
    Then the user is on the under consideration page
    When I go back to the permit application
    And I select return to permits dashboard hyperlink

  @INTERNAL @OLCS-26302
  Scenario: Only Withdraw application button is displayed on ICW on Short Term APSG permit Application
    When I'm  viewing my saved application in internal
    Then For Short term APSG, I see only Withdraw application button against on my submitted application details page