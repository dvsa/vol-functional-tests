@Deprecated
Feature: Annual bilaterals permits fee page

  Background:
    Given I have a "goods" "standard_international" licence
    And I am on the VOL self-serve site

  @EXTERNAL @OLCS-27362 @bilateral_cabotage_only
  Scenario: Verify that bilateral cabotage only permits fee page and contents are displayed correctly
    And I'm on the annual bilateral cabotage only permit fee page
    When I click the back link
    Then I should be on the bilateral overview page
    When I select the submit and pay link from overview page
    Then I should be on the permit fee page
    When I select return to overview link on the permit fee page
    Then I should be on the bilateral overview page
    When I select the submit and pay link from overview page
    Then the page heading is displayed correctly on the fee page
    And  the Fee-breakdown sub-heading can be seen below the fee summary table
    And  the application details on the fee page is displayed correctly in fee page table
    When I submit and pay
    Then I am taken to CPMS payment page