
Feature: Change semi-trailer types on self serve

  Scenario Outline: Change semi-trailer types on self serve
    Given I have a "goods" "<licenceType>" licence
    And on self serve I add a valid trailer number "<trailerNumber>" and longer semi trailer is set to "<semiTrailer>" on the licence
    When trailer number "<trailerNumber>" is changed to longer semi trailer "<newTrailer>"
    Then the trailer "<trailerNumber>" and type "<newTrailer>" is successfully added to the trailer table


    Examples:
      | licenceType            | trailerNumber | semiTrailer | newTrailer |
      | standard_international | G5456859      | Yes         | No         |
      | standard_national      | G4785632      | No          | Yes        |