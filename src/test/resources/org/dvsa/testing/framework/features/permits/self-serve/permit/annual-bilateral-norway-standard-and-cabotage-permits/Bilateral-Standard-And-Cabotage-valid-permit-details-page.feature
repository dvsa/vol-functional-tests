@bilateral_standard_and_cabotage_permits
Feature:Bilateral standard and cabotage permits: Annual bilateral valid permit-details  page checks

  Background:
    Given I have a "goods" "standard_international" licence
    And I am on the VOL self-serve site
    And I have a valid annual bilateral norway standard and cabotage permit
    And I am viewing an issued annual bilateral permit on self-serve

 @EXTERNAL @olcs-27365
  Scenario:Bilateral standard and cabotage permits:View valid permits in the licence list page
    Then the user is in the annual bilateral list page
    Then the licence number is displayed above the page heading
    Then the table of annual bilateral permits is as expected

  @EXTERNAL @olcs-27365
  Scenario:Bilateral standard and cabotage permits: Back button takes back to Permit dashboard
    When the user is in the annual bilateral list page
    And  I click the back link
    Then I should be taken to the permits dashboard

  @EXTERNAL @olcs-27365
  Scenario:Bilateral standard and cabotage permits: Returns to permit dashboard takes back to permit dashboard
    When the user is in the annual bilateral list page
    And I select return to permit dashboard hyperlink
    Then I should be taken to the permits dashboard




