
Feature: Adding trailers to a licence on internal - Longer semi and non longer semi trailers

  Scenario Outline: Adding longer semi trailers to a licence on internal
    Given I have a "goods" "<licenceType>" licence
    When on internal I add a valid trailer number "<trailerNumber>" and longer semi trailer is set to "<semiTrailer>" on the licence
    Then the trailer "<trailerNumber>" and type "<semiTrailer>" is successfully added to the trailer table


    Examples:
      | licenceType            | trailerNumber | semiTrailer |
      | standard_international | G1548563      | Yes         |
      | standard_national      | G6585632      | Yes         |
      | restricted             | G5478563      | Yes         |

  Scenario Outline: Adding non longer semi trailers to a licence on internal
    Given I have a "goods" "<licenceType>" licence
    And on internal I add a valid trailer number "<trailerNumber>" and longer semi trailer is set to "<semiTrailer>" on the licence
    Then the trailer "<trailerNumber>" and type "<semiTrailer>" is successfully added to the trailer table


    Examples:
      | licenceType            | trailerNumber | semiTrailer |
      | standard_international | G6587452      | No          |
      | standard_national      | G1125632      | No          |
      | restricted             | G5852563      | No          |

  Scenario: Trailer number mandatory field on internal
    Given I have a "goods" "standard_international" licence
    And on internal I add a trailer with no trailer number
    Then the trailer number mandatory error message appears

  Scenario: Is longer semi trailer mandatory field on internal
    Given I have a "goods" "standard_national" licence
    And on internal I add a trailer with the longer semi trailer option unanswered
    Then the is longer semi trailer mandatory error message appears