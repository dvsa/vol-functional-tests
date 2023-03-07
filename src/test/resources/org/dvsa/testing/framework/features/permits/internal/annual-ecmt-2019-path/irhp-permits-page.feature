@CLOSES-WINDOW @INTERNAL
Feature: Internal IRHP permits page

  Background:
    Given I am on the VOL self-serve site

  @OLCS-20948 @olcs-27682 @internal_annual_ecmt_apgg_euro5_or_euro6
  Scenario: Licence with no issued or ongoing ECMT permits
    Given I have a "goods" "standard_international" licence
    And i create an admin and url search for my licence
    When I am viewing a licences IRHP section
    Then the no permits applications message should be displayed

  @OLCS-20948 @olcs-27682 @internal_annual_ecmt_apgg_euro5_or_euro6
  Scenario: Licence with permit applications
    Given I have a "goods" "standard_international" licence
    And I have completed an ECMT application
    And i create an admin and url search for my licence
    When I am viewing a licences IRHP section
    Then the ongoing permit application is to be as expected
