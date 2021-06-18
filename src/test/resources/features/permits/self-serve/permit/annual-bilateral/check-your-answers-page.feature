@Deprecated
Feature: Annual bilateral check your answers page

  Background:
    Given I have a "goods" "standard_international" licence
    And I am on the VOL self-serve site
    And I'm on the annual bilateral check your answers page

  @EXTERNAL @OLCS-22909 @OLCS-26819
  Scenario: The bilateral check your answers page heading, application reference number and answers chosen should be correct and back button takes user to Overview page
    Then the bilateral check your answers page heading should be correct
    Then I am able to see the application reference number on the annual bilateral check your answers page
    Then all of the answers displayed match the answers I gave
    When I go back
    Then I should be on the bilateral overview page

  @EXTERNAL @OLCS-22909 @OLCS-26819
  Scenario: Verify that Save and continue, Save and return to overview button functionality works as expected
    When I save and continue on bilateral check your answers page
    Then I should be on the declaration page
    When I go back
    Then I navigate to the annual bilateral overview page
    When I click Check your answers link on the overview page again
    Then I am navigated to the check your answers page
    When I save and return to overview
    Then I should be on the bilateral overview page
