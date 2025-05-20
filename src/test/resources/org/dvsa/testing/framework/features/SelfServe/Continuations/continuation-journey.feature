@continuations
@gov-sign-in

  Feature: Continuations journey completed in Self Service

    @ss_regression @FullRegression @continuations_smoke @consultant
    Scenario Outline: Self Service user continues a licence
      Given as a "<userType>" I have a valid "<operatorType>" "<licenceType>" licence
      When i change my continuation and review date on Internal
      And i generate a continuation
      And fill in my continuation details on self serve as "<userType>"
      Then i click Finish and am returned to the licence overview
      Examples:
        | userType   | operatorType | licenceType            |
        | consultant | goods        | restricted             |
        | admin      | public       | special_restricted     |
