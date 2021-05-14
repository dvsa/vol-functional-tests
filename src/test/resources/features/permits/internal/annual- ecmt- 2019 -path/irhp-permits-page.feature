Feature: Internal IRHP permits page

  Background:
    Given I am on the VOL self-serve site

  @CLOSES-WINDOW @INTERNAL @OLCS-20948 @Deprecated
  Scenario: Licence with issued permits
    Given I have "9" "goods" "standard_international" licences
    When I am viewing a licence with an issued ECMT permit on internal
    Then The issued permit information should be as expected

  @CLOSES-WINDOW @INTERNAL @OLCS-20948 @olcs-27682 @internal_annual_ecmt_apgg_euro5_or_euro6
  Scenario: Licence with no issued or ongoing ECMT permits
    Given I have a "goods" "standard_international" licence
    And I am viewing a good operating licence on internal
    When I am viewing a licences IRHP section
    Then the no permits applications message should be displayed

  @CLOSES-WINDOW @INTERNAL @OLCS-20948 @olcs-27682 @internal_annual_ecmt_apgg_euro5_or_euro6
  Scenario: Licence with permit applications
    Given I have a "goods" "standard_international" licence
    And I have completed all ECMT application
    And I am viewing a good operating licence on internal
    When I am viewing a licences IRHP section
    Then the ongoing permit application is to be as expected

  @CLOSES-WINDOW @INTERNAL @OLCS-20948 @Deprecated
  Scenario: Licence without permit applications
    Given I have a "goods" "standard_international" licence
    And I am viewing a good operating licence on internal
    When I am viewing a licences IRHP section
    Then the no permits applications message should be displayed

