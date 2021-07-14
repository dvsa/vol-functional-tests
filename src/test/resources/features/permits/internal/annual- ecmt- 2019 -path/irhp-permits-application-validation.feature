@INTERNAL @internal_annual_ecmt_apgg_euro5_or_euro6 @eupa_regression

Feature: Internal ECMT permits application validation page

  Background:
    Given I have a "goods" "standard_international" licence
    And i create an admin and url search for my licence
    And I am viewing a licences IRHP section


  @olcs-20952 @olcs-27581 @olcs-27682
  Scenario: Checking number of permits rules on the application page
    When I apply for an ECMT APGG Euro5 or Euro 6 application
    When I specify more than the maximum number of permits on internal form
    And I save my IRHP permit
    Then I should get an error message in internal application

  @olcs-20952 @olcs-27682
  Scenario: Checking Cabotage validation on the application page
    When I apply for an ECMT APGG Euro5 or Euro 6 application
    When I have not declared not to undertake cabotage in internal
    And I save my IRHP permit
    Then I should get the cabotage page error message

  @olcs-20952 @olcs-27581 @olcs-27682
  Scenario: Checking Euro6 validation on the application page
    And I apply for an ECMT permit application without selecting Euro emmissions checkbox
    And I save my IRHP permit
    Then I should get the emissions  page error message

  @olcs-20952  @olcs-27581 @olcs-27682 @Deprecated
  Scenario: Checking percentage of international journeys validation on the application page
    When percentage of international journey checkbox is not selected
    And I save my IRHP permit
    Then I should get the percentage of international journeys error message

  @olcs-20952  @olcs-27581 @olcs-27682 @Deprecated
  Scenario: Checking sector page validation on the application page
    When sectors are not selected in internal
    And I save my IRHP permit
    Then I should get the sector error message

  @olcs-20952 @olcs-27682
  Scenario: Checking declaration page validation on the application page
    When declaration checkbox is not selected in internal
    And I save my IRHP permit
    Then I should not see submit button on the application page