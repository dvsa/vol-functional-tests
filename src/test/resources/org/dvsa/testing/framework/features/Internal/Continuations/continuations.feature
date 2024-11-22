@INT
@continuations
@gov-sign-in

Feature: Continuations journey through internal and self serve

  @int_regression @FullRegression @continuations_smoke
  Scenario Outline: Continue a licence that has expired
    Given as a "<user_type>" I have a valid "<operator_type>" "<licence_type>" licence
    When i change my continuation and review date on Internal
    And i generate a continuation
    And fill in my continuation details on self serve
    Then the continuation should be approved and a snapshot generated on Internal
    Examples:
      | user_type   | operator_type | licence_type            |
      | consultant  | goods         | restricted              |
      | consultant  | public        | standard_national       |
      | consultant  | public        | special_restricted      |
      | admin       | goods         | restricted              |
      | admin       | goods         | standard_national       |
      | admin       | public        | special_restricted      |

  @int_regression @FullRegression @continuations_internal @continuations_smoke
  Scenario Outline: Caseworker continues a licence that has expired
    Given i have a valid "<operatorType>" "<licenceType>" licence
    When i change my continuation and review date on Internal
    And i generate a continuation
    And a caseworkers continues my licence
    Then the continuation should be approved
    Examples:
      | operatorType | licenceType            |
      | goods        | standard_international |

  Scenario Outline: The users of ss display when reviewing a continuation
    Given i have a valid "<operatorType>" "<licenceType>" licence
    When i change my continuation and review date on Internal
    And i generate a continuation
    And i have logged in to self serve as "<user_type>"
    Then the users of ss should display on the continuation review details page and on the snapshot
    Examples:
      | operatorType | licenceType            |
      | goods        | standard_national      |
      | goods        | standard_international |
      | goods        | restricted             |
      | public       | standard_national      |
      | public       | standard_international |
      | public       | restricted             |
      | public       | special_restricted     |


  Scenario Outline: The conditions and undertaking page on a continuation displays the right text
    Given as a "<user_type>" I have a valid "<operator_type>" "<licence_type>" licence
    When i change my continuation and review date on Internal
    And i generate a continuation
    And i have logged in to self serve as "<user_type>"
    Then the continuation conditions and undertaking page and snapshot should display the right text
    Examples:
      | user_type   | operator_type | licence_type            |
      | consultant  | goods         | restricted              |
      | consultant  | public        | standard_national       |
      | consultant  | public        | special_restricted      |
      | admin       | goods         | restricted              |
      | admin       | goods         | standard_national       |
      | admin       | public        | special_restricted      |
  @WIP
  Scenario Outline: The correct checks should display when reviewing a continuation and snapshot
    Given i have a valid "<operatorType>" "<licenceType>" licence
    When i change my continuation and review date on Internal
    And i generate a continuation
    And i have logged in to self serve
    Then the correct checks should display on the continuation review details page and continuation snapshot
    Examples:
      | operatorType | licenceType            |
      | goods        | standard_national      |
      | goods        | standard_international |
      | goods        | restricted             |
      | public       | standard_national      |
      | public       | standard_international |
      | public       | restricted             |
      | public       | special_restricted     |