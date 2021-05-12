@bilateral_standard_permits_no_cabotage
Feature: Annual bilaterals standard and cabotage permits fee page

  Background:
    Given I have valid Goods standard_international VOL licence
    And I am on the VOL self-serve site

  @EXTERNAL @OLCS-27367
  Scenario: Verify that bilateral standard permits no cabotage fee page and contents are displayed correctly
    And I'm on the annual bilateral standard permit no cabotage fee page
    When I go back
    Then I should be on the bilateral overview page
    When I select the submit and pay link from overview page
    Then I should be on the permit fee page
    When I select return to overview link on the permit fee page
    Then I should be on the bilateral overview page
    When I select the submit and pay link from overview page
    Then the page heading is displayed correctly on the fee page
    And  the Fee-breakdown sub-heading can be seen below the fee summary table
    And  the application details on the bilateral standard and cabotage fee page are displayed correctly in fee page table
    When I submit and pay
    Then I am taken to CPMS payment page