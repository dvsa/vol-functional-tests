@INT
@APPLY-GOODS-LICENCE
@int_regression
@CPMS_tests
Feature: Apply for a goods licence

  Scenario: Apply for a goods restricted licence
    Given I have applied for a "goods" "restricted" licence
    When I grant licence
    Then the licence should be granted

  Scenario: Apply for a goods standard international licence
    Given I have applied for a "goods" "standard_international" licence
    When I grant licence
    Then the licence should be granted

  Scenario: Apply for a goods standard national licence
    Given I have applied for a "goods" "standard_national" licence
    When I grant licence
    Then the licence should be granted