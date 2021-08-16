@INT
@APPLY-PSV-LICENCE
@int_regression
@CPMS_tests
Feature: Apply for a psv licence

  Scenario: Apply for a psv restricted licence
    Given I have submitted a "public" "restricted" application
    When I grant licence
    Then the licence should be granted

  Scenario: Apply for a psv special restricted licence
    Given I have submitted a "public" "special_restricted" application
    When I grant licence
    Then the licence should be granted

  Scenario: Apply for a psv standard international licence
    Given I have submitted a "public" "standard_international" application
    When I grant licence
    Then the licence should be granted

  Scenario: Apply for a psv standard national licence
    Given I have submitted a "public" "standard_national" application
    When I grant licence
    Then the licence should be granted