@EXTERNAL @annual_ecmt_apgg_euro5_or_euro6
Feature: Annual ECMT Declaration page

  Background:
    Given I have a "goods" "standard_international" licence
    And  I am on the VOL self-serve site
    And I am on the application overview page
    And I am on the declaration page

  @EXTERNAL @ECMT  @OLCS-21940 @OLCS-24974 @OLCS-26709 @Test2
  Scenario: Displays validation error summary box
    When I save and continue on the declaration page
    Then I should see the validation error message for the declaration page
    And  I should see the ECMT declaration advisory texts
    And there's a guidance notes link to the correct gov page
    And I make my ECMT declaration
    And I accept and continue
    Then I should be on the ECMT permit fee page

  @EXTERNAL @ECMT @OLCS-24974 @OLCS-26709 @Test2 @olcs-27581
  Scenario: Section becomes complete when users save and accept after declaring on overview page
    When I make my ECMT declaration
    And I save and return to overview
    Then the status for the declaration section in annual ECMT is complete