
Feature: Adding trailers to a licence on self serve - Longer semi and non longer semi trailers

  Scenario Outline: Adding longer semi trailers to a licence on self serve
    Given I have a "goods" "<licenceType>" licence
    When on self serve I add a valid trailer number "<trailerNumber>" and longer semi trailer is set to "<semiTrailer>" on the licence
    Then the trailer "<trailerNumber>" and type "<semiTrailer>" is successfully added to the trailer table


    Examples:
      | licenceType            | trailerNumber | semiTrailer |
      | standard_international | G5456859      | Yes         |
      | standard_national      | G4785632      | Yes         |
      | restricted             | G1258963      | Yes         |

  Scenario Outline: Adding non longer semi trailers to a licence on self serve
    Given I have a "goods" "<licenceType>" licence
    When on self serve I add a valid trailer number "<trailerNumber>" and longer semi trailer is set to "<semiTrailer>" on the licence
    Then the trailer "<trailerNumber>" and type "<semiTrailer>" is successfully added to the trailer table


    Examples:
      | licenceType            | trailerNumber | semiTrailer |
      | standard_international | G5478521      | No          |
      | standard_national      | G1526398      | No          |
      | restricted             | G1258964      | No          |

  Scenario: Trailer number mandatory field on self serve
    Given I have a "goods" "standard_international" licence
    When on self serve I add a trailer with no trailer number
    Then the trailer number mandatory error message appears

  Scenario: Is longer semi trailer mandatory field on self serve
    Given I have a "goods" "standard_national" licence
    When on self serve I add a trailer with the longer semi trailer option unanswered
    Then the is longer semi trailer mandatory error message appears