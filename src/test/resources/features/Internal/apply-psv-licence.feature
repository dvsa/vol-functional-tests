@INT
@APPLY-PSV-LICENCE
@int_regression
@CPMS_tests
Feature: Apply for a psv licence

  Scenario: Apply for a restricted licence
    Given I have applied for a "public" "restricted" licence
    When I grant licence
    Then the licence should be granted

  Scenario: Apply for a special restricted licence
    Given I have applied for a "public" "special_restricted" licence
    When I grant licence
    Then the licence should be granted

  Scenario: Apply for a standard international licence
    Given I have applied for a "public" "standard_international" licence
    When I grant licence
    Then the licence should be granted

  Scenario: Apply for a standard national licence
    Given I have applied for a "public" "standard_national" licence
    When I grant licence
    Then the licence should be granted