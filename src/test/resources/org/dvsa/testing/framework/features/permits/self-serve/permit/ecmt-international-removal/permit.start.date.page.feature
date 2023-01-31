@ecmt_removal @eupa_regression
Feature: ECMT International Removals Certificates required  Page

  Background:
    Given I have a "goods" "standard_national" licence
    And I am on the ECMT removals permit start page

  @EXTERNAL @r222gremovalsfix
  Scenario: Application back button takes back to overview page
    When I click the back link
    Then I should be on the overview page

  @EXTERNAL @r222gremovalsfix
  Scenario: Permit Start Date Page functionalities work properly
    And  the reference number is displayed correctly
    And  the page heading on permit start date page should be correct
    And  the advisory texts on permit start date page are displayed correctly
    When save and continue  button is selected without selecting the checkbox
    Then the error message is displayed in the permit start date page
    When I save and return to overview
    Then the error message is displayed in the permit start date page
    When I dont enter all the fields
    And  I click save and continue
    Then the error message is displayed in the permit start date page
    When I enter invalid date
    And  I click save and continue
    Then the error message is displayed in the permit start date page
    When I enter a date ahead of 60 days
    And  I click save and continue
    Then I should get a valid error message
    When I enter the valid date
    And  I click save and continue
    Then I am taken to the number of permits page