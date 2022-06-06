
Feature: Adding trailers to a licence - Longer semi and non longer semi trailers

  Scenario Outline: Adding longer semi trailers to a licence
    Given I have a "goods" "<licenceType>" licence
    When I add a valid trailer number "<trailerNumber>" and longer semi trailer is set to "<semiTrailer>" on the licence
    Then the trailer "<trailerNumber>" and type "<semiTrailer>" is successfully added to the trailer table


    Examples:
      | licenceType            | trailerNumber | semiTrailer |
      | standard_international | GHTYU67       | Yes         |
      | standard_national      | GHTYU67       | Yes         |
      | restricted             | GHTYU67       | Yes         |

  Scenario Outline: Adding non longer semi trailers to a licence
    Given I have a "goods" "<licenceType>" licence
    And I add a valid trailer number "<trailerNumber>" and longer semi trailer is set to "<semiTrailer>" on the licence
    Then the trailer "<trailerNumber>" and type "<semiTrailer>" is successfully added to the trailer table


    Examples:
      | licenceType            | trailerNumber | semiTrailer |
      | standard_international | GHTYU67       | No          |
      | standard_national      | GHTYU67       | No          |
      | restricted             | GHTYU67       | No          |

  Scenario: Trailer number mandatory field
    Given I have a "goods" "standard_international" licence
    And I add a trailer with no trailer number
    Then the trailer number mandatory error message appears

  Scenario: Is longer semi trailer mandatory field
    Given I have a "goods" "standard_national" licence
    And I add a trailer with the longer semi trailer option unanswered
    Then the is longer semi trailer mandatory error message appears