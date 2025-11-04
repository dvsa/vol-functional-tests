@EXTERNAL @ecmt_removal @eupa_regression
Feature: ECMT International Number of permits Page

  Background:
    Given I have a "goods" "standard_international" licence
    And I am on the ECMT Removal number of permits page

  @ss_regression
  Scenario: Number of permits page ECMT Removal - Check validations and content on the page
    Then the reference number and heading are displayed correct
    And the fee information is displayed correctly
    When I enter zero as value in the number of permits fields
    And I save and continue
    Then the relevant error message is displayed
    When I select save and return overview link
    Then the relevant error message is displayed
    When I enter a number exceeding the maximum authorised vehicles
    And I save and continue
    Then the maximum exceeded error message is displayed
    When I specify my number of ECMT removal permits

  Scenario: Number of permits page ECMT Removal- Check Navigation and section status
    When I enter valid number of permits
    And I select save and return overview link
    Then the number of permits section on the ECMT Removals Overview page is complete