@EXTERNAL @ecmt_removal @eupa_regression
Feature: ECMT International Number of permits Page

  Background:
    Given I have a "goods" "standard_international" licence
    And I am on the ECMT Removal number of permits page

  @reads-and-writes-system-properties
  Scenario: Number of permits page ECMT Removal - Check validations and content on the page
    Then the page heading on the ECMT removals number of permits page is displayed correctly
    And  the application reference on the ECMT removals number of permits page is displayed correctly
    And the advisory text on the ECMT removals number of permits page is displayed correctly
    Then I should get the number of permits page error message
    And I save and return to overview
    Then I should get the number of permits page error message
    And I enter number of permits more than the authorised vehicles and click save and continue
    Then I should get the maximum number of permits exceeded page error message
    And I enter valid number of permits on the removals number of permits page and click save and continue

  @reads-system-properties
  Scenario: Number of permits page ECMT Removal- Check Navigation and section status
    When I enter valid number of permits on the removals number of permits page
    And I save and return to overview
    Then the number of permits section on the ECMT Removals Overview page is complete