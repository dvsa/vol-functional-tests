#permit type is not being used at the moment
@MULTILATERAL
Feature: Check your answers page

  Background:
      Given I have valid Goods standard_national VOL licence
      And I am on the VOL self-serve site
      And I am on the annual multilateral check your answers page

  #AC01
  @OLCS-23016
  Scenario: Back button returns to overview page
    When I go back
    Then I should be on the Annual Multilateral overview page

  #AC02
  @OLCS-23016
  Scenario: Has application reference displayed
    Then the annual bilateral check your answers page has an application reference displayed

  #AC05, AC06, and AC07
  @OLCS-23016 @olcs-27581
  Scenario: Body contains answers from those chosen during the application process
    Then Annual multilateral application answers are displayed on the check your answers page

  #AC08
  @OLCS-23016
  Scenario: Saving and continuing takes the user to the declaration page and marks section as complete
      And I confirm and continue
      And I should be on the Annual Multilateral Declaration page
      When I make my declaration
      And I save and return to overview
      Then the section is marked as complete on annual multilateral overview page

  #AC09
  @OLCS-23016
  Scenario: Saving and returning to overview marks section as complete and takes user to overview page
    When I save and return to overview
    Then I should be on the Annual Multilateral overview page
    And the section is marked as complete on annual multilateral overview page