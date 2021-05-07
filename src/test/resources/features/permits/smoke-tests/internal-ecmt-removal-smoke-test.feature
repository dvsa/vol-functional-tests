@smoketest
Feature: ECMT removal application Internal End to End smoke test

  Background:
    Given I have valid Goods standard_international VOL licence
    And I am on the VOL internal site
    And I am viewing a good operating licence on internal

  @OLCS-28261
  Scenario: Case worker submits ECMT removal application successfully via Internal
    When the case worker submits partial ECMT Removal application
    And I pay fee for the ECMT removal application
    Then the application goes to valid status