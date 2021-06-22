@EXTERNAL @ecmt_removal
Feature: ECMT International Number of permits Page

  Background:
    Given I have a "goods" "standard_international" licence
    And  I am on the VOL self-serve site


  Scenario: Number of permits page ECMT Removal - Check validations and content on the page
    And I am on the ECMT Removal number of permits page
    Then the page heading on the ECMT removals number of permits page is displayed correctly
    And  the application reference on the ECMT removals number of permits page is displayed correctly
    And the advisory text on the ECMT removals number of permits page is displayed correctly
    When I have not entered anything in number of  permits field
    And  I save and continue
    Then I should get the number of permits page error message
    And I save and return to overview
    Then I should get the number of permits page error message
    And I enter number of permits more than the authorised vehicles and click save and continue
    Then I should get the number of permits page error message
    And I enter valid number of permits on the removals number of permits page and click save and continue
   Then the page heading on check your answers page is correct

  Scenario: Number of permits page ECMT Removal- Check Navigation and section status
    And I am on the ECMT Removal number of permits page
    When I enter valid number of permits on the removals number of permits page
    And I save and return to overview
    Then the number of permits section on the ECMT Removals Overview page is complete