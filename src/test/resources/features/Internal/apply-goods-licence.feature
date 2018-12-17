@INT
@APPLY-GOODS-LICENCE
Feature: Apply for a goods licence
  Scenario: Apply for a restricted licence
    Given I have applied for a "goods" "restricted" licence
    When I grant licence
    Then the licence should be granted

  Scenario: Apply for a standard international licence
    Given I have applied for a "goods" "standard_international" licence
    When I grant licence
    Then the licence should be granted

  Scenario: Apply for a standard national licence
    Given I have applied for a "goods" "standard_national" licence
    When I grant licence
    Then the licence should be granted