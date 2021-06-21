@EXTERNAL @annual_ecmt_apgg_euro5_or_euro6 @eupa_regression
Feature: Display of the Self service Permit tab

  Background:
    Given I have a valid Goods standard_international VOL licence
    And  I am on the VOL self-serve site

  @EXTERNAL @OLCS-20685 @ECMT @Test3
  Scenario: User has the required VOL licence type
    When I login to self-serve on VOL
    Then the permits tab should be displayed

  # Added WIP tag due to an inability to create a special restricted licence using api-action at the moment
  @WIP @EXTERNAL @OLCS-20685 @ECMT @Deprecated
  Scenario: User does not have the required licence type
    Given I have a valid Goods special_restricted VOL licence
    When I login to self-serve on VOL
    Then the permits tab should not be displayed
# TODO: Find out how special_restricted licences are created as operators cannot create them themselves.
# {"messages":{"licenceType":[{"AP-TOL-2":"GV operators cannot apply for special restricted licences"}]}}