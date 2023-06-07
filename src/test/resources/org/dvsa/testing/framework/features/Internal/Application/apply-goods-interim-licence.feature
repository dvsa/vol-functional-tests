@INT
@GRANT-GOODS-INTERIM-APP
@CPMS_tests
@int_regression
@FullRegression

Feature: apply for interim goods licence

  Background:
    Given I have a "goods" "standard_national" application which is under consideration

  Scenario: Apply for interim goods licence
    When I grant licence
    Then the licence should be granted