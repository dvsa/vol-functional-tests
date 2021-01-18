@INT
@APPLY-GOODS-LICENCE
@int_regression
@CPMS_tests
Feature: Apply for a goods licence

  Scenario: Apply for a goods restricted licence
    Given I have submitted a "goods" "restricted" application
    When I grant licence
    Then the licence should be granted

  Scenario: Apply for a goods standard international licence
    Given I have submitted a "goods" "standard_international" application
    When I grant licence
    Then the licence should be granted

  Scenario: Apply for a goods standard national licence
    Given I have submitted a "goods" "standard_national" application
    When I grant licence
    Then the licence should be granted