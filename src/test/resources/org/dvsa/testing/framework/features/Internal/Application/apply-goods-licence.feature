@INT
@APPLY-GOODS-LICENCE
@int_regression
@CPMS_tests
Feature: Apply for a goods licence

  Scenario Outline: Apply for a goods licence
    Given I have a submitted "<operator>" "<licenceType>" application
    When I grant licence
    Then the licence should be granted
    Examples:
      | operator | licenceType            |
      | goods    | restricted             |
      | goods    | standard_international |
      | goods    | standard_national      |