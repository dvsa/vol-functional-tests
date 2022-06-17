@INT
@APPLY-PSV-LICENCE
@int_regression
@CPMS_tests
Feature: Apply for a psv licence

  @psv_r
  Scenario: Apply for a psv restricted licence
    Given I have a submitted "public" "restricted" application
    When I grant licence
    Then the licence should be granted

  @psv_sr
  Scenario: Apply for a psv special restricted licence
    Given I have a submitted "public" "special_restricted" application
    When I grant licence
    Then the licence should be granted

  @psv_si
  Scenario: Apply for a psv standard international licence
    Given I have a submitted "public" "standard_international" application
    When I grant licence
    Then the licence should be granted

  @psv_sn
  Scenario: Apply for a psv standard national licence
    Given I have a submitted "public" "standard_national" application
    When I grant licence
    Then the licence should be granted