@Deprecated
Feature: Bilateral cabotage only permits

  Background:
    Given I have a "goods" "standard_international" licence
    And I am on the VOL self-serve site
    And I have a valid annual bilateral noway cabotage only permit
    And I am viewing an issued annual bilateral permit on self-serve

  @OLCS-23288 @EXTERNAL @OLCS-26045 @OLCS-26819 @olcs-27365
  Scenario: Bilateral cabotage only permits:View valid permits in the licence list page
    Then the user is in the annual bilateral list page
    And the licence number is displayed in Annual bilateral list page
    When I select Norway in the filter list and click Apply filter
    Then the table of annual bilateral permits is as expected

  @OLCS-23288 @EXTERNAL @OLCS-26045 @olcs-27365
  Scenario:Bilateral cabotage only permits: Back button takes back to Permit dashboard
    When the user is in the annual bilateral list page
    And  I go back
    Then I should be taken to the permits dashboard

  @OLCS-23288 @EXTERNAL @OLCS-26045 @olcs-27365
  Scenario:Bilateral cabotage only permits: Returns to permit dashboard takes back to permit dashboard
    When the user is in the annual bilateral list page
    And I select returns to permit dashboard hyperlink
    Then I should be taken to the permits dashboard