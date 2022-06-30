@EXTERNAL @ECMT @annual_ecmt_apgg_euro5_or_euro6 @eupa_regression
Feature: Annual ECMT Declaration page

  Background:
    Given I have a "goods" "standard_international" licence
    And I am on the application overview page
    And I fill all steps preceding steps to declaration

  @OLCS-21940 @OLCS-24974 @OLCS-26709
  Scenario: Displays validation error summary box
    When I save and continue on the declaration page
    Then I should see the validation error message for the declaration page
    And  I should see the declaration advisory texts
    And there's a guidance notes link to the correct gov page
    And I confirm the declaration
    And I accept and continue
    Then I should be on the ECMT permit fee page

  @OLCS-24974 @OLCS-26709 @olcs-27581
  Scenario: Section becomes complete when users save and accept after declaring on overview page
    When I confirm the declaration
    And I save and return to overview
    Then the status for the declaration section in annual ECMT is complete