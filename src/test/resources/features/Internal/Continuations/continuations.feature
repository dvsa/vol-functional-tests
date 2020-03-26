@ss_regression
@continuations

Feature: Continuations journey through internal and self serve

  Scenario: Continue a licence that has expired
    Given I have applied for a "goods" "standard_national" licence
    When I grant licence
    Then the licence should be granted
    And i have logged in to internal
    When i change my continuation date
    And i generate a continuation
    And fill in my continuation details on self serve
    Then the continuation should be approved and a snapshot generated on Internal

  Scenario: The users of ss display when reviewing a continuation
    Given I have applied for a "goods" "standard_national" licence
    When I grant licence
    Then the licence should be granted
    And i have logged in to internal
    When i change my continuation date
    And i generate a continuation
    And i have logged in to self serve
    Then the users of ss should display on the continuation review details page and on the snapshot

  Scenario Outline: The conditions and undertaking page on a continuation displays the right text
    Given i have a valid "<operatorType>" "<licenceType>" licence
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

  Scenario: The correct checks should display when reviewing a continuation
    Given I have applied for a "goods" "standard_national" licence
    When I grant licence
    Then the licence should be granted
    And i have logged in to internal
    When i change my continuation date
    And i generate a continuation
    And i have logged in to self serve
    Then the correct checks should display on the continuation review details page

Scenario: The correct checks should display on the continuation snapshot
    Given I have applied for a "goods" "standard_national" licence
    When I grant licence
    Then the licence should be granted
    And i have logged in to internal
    When i change my continuation date
    And i generate a continuation
    And i have logged in to self serve
    Then the correct check should display on the continuation snapshot
