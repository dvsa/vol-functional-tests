
Feature: Adding trailers to a licence - Longer semi and non longer semi trailers

  Scenario Outline: Adding longer semi trailers to a licence
    Given I have a "goods" "<licenceType>" licence
    And I add a valid trailer number and longer semi trailer is set to yes
    When I save the Trailers Page
    Then the trailer is successfully added to the trailer table


    Examples:
      | licenceType            |
      | standard_international |
      | standard_national      |
      | restricted             |

  Scenario Outline: Adding non longer semi trailers to a licence
    Given I have a "goods" "<licenceType>" licence
    And I add a valid trailer number and longer semi trailer is set to no
    When I save the Trailers Page
    Then the trailer is successfully added to the trailer table


    Examples:
      | licenceType            |
      | standard_international |
      | standard_national      |
      | restricted             |
