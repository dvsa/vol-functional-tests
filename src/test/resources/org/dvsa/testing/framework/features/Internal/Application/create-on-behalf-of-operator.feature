@printAndSign
@OLCS-24241
@Create-On-Behalf-Of-User
@int_regression
@FullRegression

Feature: Internal User should be able to create an operator account

  @reads-and-writes-system-properties
  Scenario Outline: Caseworker submits application
    Given I have a "<operator>" "<licence-type>" application
    When the caseworker completes and submits the application
    And grants the application
    Then the licence is granted in Internal
    And the "<document>" document should be generated

    Examples:
      | operator | licence-type           | document    |
      | public   | restricted             | PSV Licence |
      | public   | standard_national      | PSV Licence |
      | public   | standard_international | PSV Licence |