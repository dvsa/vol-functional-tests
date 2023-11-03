@INT
@APPLY-PSV-LICENCE
@int_regression
@CPMS_tests
@FullRegression
@APIsmoke

Feature: Apply for a psv licence

  Scenario Outline: Apply for a psv licence
    Given I have a submitted "<operator>" "<licenceType>" application
    When I grant licence
    Then the licence should be granted
    Examples:
      | operator | licenceType            |
      | public   | restricted             |
      | public   | special_restricted     |
      | public   | standard_international |
      | public   | standard_national      |