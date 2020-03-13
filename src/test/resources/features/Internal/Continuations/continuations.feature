@ss_regression
@continuations

Feature: Continuations journey through internal and self serve

  Scenario: Continue a licence that has expired
    Given i have a valid "goods" "standard_national" licence
    And i have logged in to internal
    When i change my continuation date
    And i generate a continuation
    And fill in my continuation details on self serve
    Then the continuation should be approved and a snapshot generated on Internal

  Scenario: The users of ss display when reviewing a continuation
    Given i have a valid "goods" "standard_national" licence
    And i have logged in to internal
    When i change my continuation date
    And i generate a continuation
    And i have logged in to self serve
    Then the users of ss should display on the continuation review details page

  Scenario Outline: The conditions and undertaking page on a continuation displays the right text
    Given i have a valid "goods" "standard_national" licence
    And i have logged in to internal
    When i change my continuation date
    And i generate a continuation
    And i have logged in to self serve
    Then the continuation for a "<operatorType>" "<licenceType>" conditions and undertaking page should display the right text
    Examples:
      | operatorType | licenceType            |
      | goods        | standard_national      |
      | goods        | standard_international |
      | goods        | restricted             |
      | public       | standard_national      |
      | public       | standard_international |
      | public       | restricted             |
      | public       | special_restricted     |