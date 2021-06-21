@internal_annual_ecmt_apgg_euro5_or_euro6 @eupa_regression
Feature: Internal Licence details page

  Background:
    Given I am on the VOL self-serve site

  @INTERNAL @OLCS-20940
  Scenario: Display of IRHP Permits tab when operating licence is for goods
    Given I have valid Goods standard_international VOL licence
    And I have completed an ECMT application
    And I am viewing a good operating licence on internal
    Then I should see the IRHP permits tab

  @INTERNAL @OLCS-20940
  Scenario: Condition to display IRHP Permits tab is not met
    Given I have valid PublicService standard_national VOL licence
    And I am viewing a good operating licence on internal
    Then I should not see the IRHP permits tab
