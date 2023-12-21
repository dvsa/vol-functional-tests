@INTERNAL @OLCS-20940 @internal_annual_ecmt_apgg_euro5_or_euro6 @OLCS-25288 @eupa_regression
Feature: Internal Licence details page

  Background:
    Given I am on the VOL self-serve site

  @reads-and-writes-system-properties
  Scenario: Display of IRHP Permits tab when operating licence is for goods
    Given I have a "goods" "standard_international" licence
    And i create an admin and url search for my licence
    Then I should see the IRHP permits tab

  @reads-system-properties
  Scenario: Condition to display IRHP Permits tab is not met
    Given I have a "public" "standard_international" licence
    And i create an admin and url search for my licence
    Then I should not see the IRHP permits tab
