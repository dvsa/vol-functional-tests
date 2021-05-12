Feature: Internal ECMT permits application validation page

  Background:
    Given I have valid Goods standard_international VOL licence
    And I am on the VOL internal site
    And I am viewing a good operating licence on internal
    And I am viewing a licences IRHP section


  @INTERNAL @olcs-20952 @olcs-27581 @internal_annual_ecmt_apgg_euro5_or_euro6 @olcs-27682
  Scenario: Checking number of permits rules on the application page
    When I apply for an ECMT APGG Euro5 or Euro 6 application
    When I specify more than the maximum number of permits on internal form
    And I save my IRHP permit
    Then I should get an error message in internal application

  @INTERNAL @olcs-20952 @internal_annual_ecmt_apgg_euro5_or_euro6 @olcs-27682
  Scenario: Checking Cabotage validation on the application page
    When I apply for an ECMT APGG Euro5 or Euro 6 application
    When I have not declared not to undertake cabotage in internal
    And I save my IRHP permit
    Then I should get the cabotage page error message

  @INTERNAL @olcs-20952  @olcs-27581 @internal_annual_ecmt_apgg_euro5_or_euro6 @olcs-27682
  Scenario: Checking Euro6 validation on the application page
    And I apply for an ECMT permit application without selecting Euro emmissions checkbox
    And I save my IRHP permit
    Then I should get the emissions  page error message

  @INTERNAL @olcs-20952  @olcs-27581 @internal_annual_ecmt_apgg_euro5_or_euro6 @olcs-27682 @Deprecated
  Scenario: Checking percentage of international journeys validation on the application page
    When percentage of international journey checkbox is not selected
    And I save my IRHP permit
    Then I should get the percentage of international journeys error message

  @INTERNAL @olcs-20952  @olcs-27581 @internal_annual_ecmt_apgg_euro5_or_euro6 @olcs-27682 @Deprecated
  Scenario: Checking sector page validation on the application page
    When sectors are not selected in internal
    And I save my IRHP permit
    Then I should get the sector error message

  @INTERNAL @olcs-20952 @internal_annual_ecmt_apgg_euro5_or_euro6 @olcs-27682
  Scenario: Checking declaration page validation on the application page
    When declaration checkbox is not selected in internal
    And I save my IRHP permit
    Then I should not see submit button on the application page