@EXTERNAL @ECMT @annual_ecmt_apgg_euro5_or_euro6 @eupa_regression
Feature: Number of permits required page

  Background:
    Given I have a "goods" "standard_international" licence
    And I am on the VOL self-serve site
    And I am on the number of permits page

  @OLCS-24823 @OLCS-27781 @OLCS-28275
  Scenario: Page Heading and Advisory texts and validation messages are displayed
    And the page heading on the ECMT number of permits page is displayed correctly
    And the advisory texts are displayed correctly
    And I save and continue
    And I should get the number of permits page error message on short term
    And I save and return to overview
    And I should get the number of permits page error message on short term
    And I enter the valid number of short term permits required
    And I save and return to overview
    Then I should be on the Annual ECMT overview page

  @OLCS-21286 @OLCS-24823 @DEV @OLCS-28275
  Scenario: Specifies a number larger than their number of authorised vehicles
    Given I have specified a number greater than the number of authorised vehicles
    When I save and continue
    Then I should get an error message

  @OLCS-21286 @DEV @olcs-27581 @OLCS-28275
  Scenario: Application back button
    When I click the back link
    Then I should be on the Annual ECMT overview page

  @OLCS-17815 @olcs-27581 @OLCS-27781 @OLCS-28275
  Scenario: Pay Fees from Fee Tab on Self serve screen
    And I enter the valid number of short term permits required
    When I save and return to overview
    Then the overview page heading is displayed correctly
    When I click the back link
    Then I should be on the permits dashboard page with an ongoing application
    When I select the fee tab and pay the outstanding fees
    Then I am taken to the payment successful page