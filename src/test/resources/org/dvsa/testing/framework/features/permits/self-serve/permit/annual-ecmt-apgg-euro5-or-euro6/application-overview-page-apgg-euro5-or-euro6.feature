@EXTERNAL @ECMT @annual_ecmt_apgg_euro5_or_euro6
Feature: Application overview page

  Background:
    Given I have a "goods" "standard_international" licence
    And I am on the application overview page

  @OLCS-24821
  Scenario Outline: Specific sections are disabled if there's a preceding step that's incomplete
    Then the <section> section should be disabled

    Examples:
    | section            |
    | check your answers |
    | declaration        |


  @OLCS-24821 @olcs-27581
  Scenario: Check your answers is enabled if all preceding steps are completed
    When I have completed all steps prior to check your answers page
    And I click the back link
    Then the check your answers section should be enabled

  @OLCS-24821 @olcs-27581
  Scenario: Declaration is enabled if all preceding steps are completed
    When I fill all steps preceding steps to declaration
    And I click the back link
    Then the declaration section should be enabled

  @OLCS-24821 @olcs-27581
  Scenario: Application overview functionalities are displayed correctly
    Then only the expected status labels are displayed
    And  the overview page heading is displayed correctly
    When I click the back link
    Then I should be taken to the permits dashboard