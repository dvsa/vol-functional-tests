#permit type is not being used at the moment
@MULTILATERAL
Feature: Declaration page

  Background:
    Given I have a "goods" "standard_international" licence
    And I am on the VOL self-serve site
    And I am on the annual multilateral declaration page

  #AC01
  @OLCS-23017
  Scenario: Application back button
    When I click the back link
    Then I should be on the Annual Multilateral overview page

  #AC02
  @OLCS-23017 @OLCS-26046 @olcs-27502
  Scenario: Has application reference number
    Then the declaration page has a reference number
    And  annual multilateral declaration page has advisory messages
    And  the annual multilateral declaration checkbox is unselected
    When I save and continue on the declaration page
    Then I should get an error message
    When I save and return to overview
    Then I should get an error message
    And I confirm the declaration
    When I save and return to overview
    Then the status for the declaration section in annual multilateral is complete

  @OLCS-23017
  Scenario: Accepting and continuing completes section
    And I confirm the declaration
    When I save and continue on the declaration page
    And I save and return to overview on multilateral fee page
    Then the status for the declaration section in annual multilateral is complete