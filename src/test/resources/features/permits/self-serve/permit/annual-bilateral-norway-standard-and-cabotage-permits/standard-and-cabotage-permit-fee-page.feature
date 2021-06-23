@bilateral_standard_and_cabotage_permits
Feature: Annual bilaterals standard and cabotage permits fee page

  Background:
    Given I have a "goods" "standard_international" licence
    And I am on the VOL self-serve site

  @EXTERNAL @OLCS-27367
  Scenario: Verify that bilateral standard and cabotage permits fee page and contents are displayed correctly when cabotage is not selected
    And I'm on the annual bilateral standard and cabotage permit fee page
    When I click the back link
    Then I should be on the overview page
    When I select the submit and pay link from overview page
    Then I should be on the permit fee page
    When I select return to overview link on the permit fee page
    Then I should be on the overview page
    When I select the submit and pay link from overview page
    Then the page heading is displayed correctly on the fee page
    And  the Fee-breakdown sub-heading can be seen below the fee summary table
    And  the application details on the bilateral standard and cabotage fee page are displayed correctly in fee page table
    When I submit and pay
    Then I am taken to CPMS payment page

  @EXTERNAL @OLCS-27367
  Scenario: Verify that bilateral standard and cabotage permits fee page and contents are displayed correctly when Cabotage is selected
    And I'm on the annual bilateral standard and cabotage permit fee page with Cabotage selected on Cabotage page
    When I click the back link
    Then I should be on the overview page
    When I select the submit and pay link from overview page
    Then I should be on the permit fee page
    When I select return to overview link on the permit fee page
    Then I should be on the overview page
    When I select the submit and pay link from overview page
    Then the page heading is displayed correctly on the fee page
    And  the Fee-breakdown sub-heading can be seen below the fee summary table
    And  the application details on the bilateral standard and cabotage fee page are displayed correctly in fee page table when cabotage is selected
    When I submit and pay
    Then I am taken to CPMS payment page