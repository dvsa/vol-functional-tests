
Feature: Adding trailers to a licence - Longer semi and non longer semi trailers

  Scenario Outline: Adding longer semi trailers to a licence
    Given I have a "goods" "<licenceType>" licence
    When I add a valid trailer number "<trailerNumber>" and longer semi trailer is set to "<semiTrailer>" on the licence
    Then the trailer is successfully added to the trailer table


    Examples:
      | licenceType            | trailerNumber | semiTrailer |
      | standard_international | GHTYU67       | Yes         |
      #| standard_national      | GHTYU67       | Yes         |
      #| restricted             | GHTYU67       | Yes         |

  Scenario Outline: Adding non longer semi trailers to a licence
    Given I have a "goods" "<licenceType>" licence
    And I add a valid trailer number "<trailerNumber>" and longer semi trailer is set to "<semiTrailer>" on the licence
    Then the trailer is successfully added to the trailer table


    Examples:
      | licenceType            | trailerNumber | semiTrailer |
      | standard_international | GHTYU67       | Yes         |
      | standard_national      | GHTYU67       | Yes         |
      | restricted             | GHTYU67       | Yes         |
