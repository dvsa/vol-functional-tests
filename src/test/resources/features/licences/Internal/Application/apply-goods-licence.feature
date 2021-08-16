@INT
@APPLY-GOODS-LICENCE
@int_regression
@CPMS_tests
Feature: Apply for a goods licence

  @goods_r
  Scenario: Apply for a goods restricted licence
    Given I have submitted a "goods" "restricted" application
    When I grant licence
    Then the licence should be granted

  @goods_si
  Scenario: Apply for a goods standard international licence
    Given I have submitted a "goods" "standard_international" application
    When I grant licence
    Then the licence should be granted

  @goods_sn
  Scenario: Apply for a goods standard national licence
    Given I have submitted a "goods" "standard_national" application
    When I grant licence
    Then the licence should be granted