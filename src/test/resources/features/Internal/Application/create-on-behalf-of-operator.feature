@INT
@OLCS-24241
@Create-On-Behalf-Of-User
@int_regression
Feature: Internal User should be able to create an operator account

  Scenario Outline: Caseworker submits application
      Given I have partially applied for a "<operator>" "<licence-type>" licence
      When the caseworker completes and submits the application
      And grants the application
      Then the licence is granted in Internal
      And the "<document>" document is produced automatically

      Examples:
      | operator | licence-type           | document    |
      | public   | restricted             | PSV Licence |
      | public   | standard_national      | PSV Licence |
      | public   | standard_international | PSV Licence |