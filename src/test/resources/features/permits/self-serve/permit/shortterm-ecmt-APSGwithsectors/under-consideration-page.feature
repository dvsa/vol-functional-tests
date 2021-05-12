@Deprecated
Feature: Short term ECMT APSG with sectors under consideration page

  Background:
    Given I have valid Goods standard_international VOL licence
    And  I am on the VOL self-serve site
    And  I am on Short term under consideration page

  @OLCS-25670
  Scenario: Under Consideration page details are displayed correctly
    Then the page heading on under consideration page is displayed correctly
    And the table of contents in the short term  under consideration page are displayed correctly
    And the warning message is displayed correctly
    When I select withdraw application button
    Then I am taken to the Withdraw Application page
    When I go back
    Then I am taken back to Under Consideration Page
    When I go back
    Then the user is on self-serve permits dashboard
    When I go back to the permit application
    And I select return to permits dashboard hyperlink
    Then the user is on self-serve permits dashboard

  @INTERNAL @OLCS-26302
  Scenario: Only Withdraw application button is displayed on ICW on Short Term APSG permit Application
    When I'm  viewing my saved application in internal
    Then For Short term APSG, I see only Withdraw application button against on my submitted application details page