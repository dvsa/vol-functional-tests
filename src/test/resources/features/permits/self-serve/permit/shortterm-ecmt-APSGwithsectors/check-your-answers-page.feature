@Deprecated
Feature: Short term ECMT APSG with sectors Check your answers page

  Background:
    Given I have a "goods" "standard_international" licence
    And  I am on the VOL self-serve site
    And  I am on the Short term check your answers page

  @EXTERNAL @OLCS-25698 @olcs-27581
  Scenario: Verify that content and various functions on the Page are as per AC's defined in olcs-25698
    Then short term permit check your answers page has correct heading label
    Then the short term check your answers page has reference number
    Then Short term application answers are displayed on the check your answers page
    When I save and return to overview
    Then I am on the short term permits overview page with check your answers section marked as complete
    Then the declaration section gets enabled to be clicked and section status changes to NOT STARTED YET
    And  I click Check your answers link on the overview page again
    Then I am navigated to the short term check your answers page
    When I go back
    Then  I am on the short term permits overview page with check your answers section marked as complete