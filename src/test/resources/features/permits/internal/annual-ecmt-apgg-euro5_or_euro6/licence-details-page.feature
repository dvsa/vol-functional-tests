@INTERNAL @OLCS-20940 @internal_annual_ecmt_apgg_euro5_or_euro6 @OLCS-25288
Feature: Internal Licence details page

  Background:
    Given I am on the VOL self-serve site

  Scenario: Display of IRHP Permits tab when operating licence is for goods
    Given I have a "goods" "standard_international" licence
    And I have completed an ECMT application
    And i create an admin and url search for my licence
    Then I should see the IRHP permits tab

  Scenario: Condition to display IRHP Permits tab is not met
    Given I have a "public" "standard_international" licence
    And i create an admin and url search for my licence
    Then I should not see the IRHP permits tab
