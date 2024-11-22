@INT
@continuations
@gov-sign-in

Feature: Continuations journey through internal and self serve

  @int_regression @FullRegression @continuations_smoke
  Scenario Outline: Continue a licence that has expired
    Given as a "<userType>" I have a valid "<operatorType>" "<licenceType>" licence
    When i change my continuation and review date on Internal
    And i generate a continuation
    And fill in my continuation details on self serve
    Then the continuation should be approved and a snapshot generated on Internal
    Examples:
      | userType   | operatorType | licenceType            |
      | consultant | goods        | restricted             |
      | admin      | public       | standard_national      |
      | admin      | public       | special_restricted     |

  @int_regression @FullRegression @continuations_internal @continuations_smoke
  Scenario Outline: Caseworker continues a licence that has expired
    Given as a "<userType>" I have a valid "<operatorType>" "<licenceType>" licence
    When i change my continuation and review date on Internal
    And i generate a continuation
    And a caseworker continues my licence
    Then the continuation should be approved
    Examples:
      | userType   | operatorType | licenceType            |
      | consultant | goods        | standard_international |
      | admin      | public       | standard_international |

  Scenario Outline: The users of ss display when reviewing a continuation
    Given as a "<userType>" I have a valid "<operatorType>" "<licenceType>" licence
    When i change my continuation and review date on Internal
    And i generate a continuation
    And i have logged in to self serve as "<userType>"
    Then the users of ss should display on the continuation review details page and on the snapshot
    Examples:
      | userType   | operatorType | licenceType            |
      | consultant | goods        | standard_national      |
      | admin      | goods        | standard_international |
      | admin      | goods        | restricted             |
      | consultant | public       | standard_national      |
      | admin      | public       | standard_international |
      | admin      | public       | restricted             |
      | consultant | public       | special_restricted     |

  Scenario Outline: The conditions and undertaking page on a continuation displays the right text
    Given as a "<userType>" I have a valid "<operatorType>" "<licenceType>" licence
    When i change my continuation and review date on Internal
    And i generate a continuation
    And i have logged in to self serve as "<userType>"
    Then the continuation conditions and undertaking page and snapshot should display the right text
    Examples:
      | userType   | operatorType | licenceType            |
      | consultant | goods        | restricted              |
      | consultant | public        | standard_national       |
      | consultant | public        | special_restricted      |
      | admin      | goods         | restricted              |
      | admin      | goods         | standard_national       |
      | admin      | public        | special_restricted      |

  @WIP
  Scenario Outline: The correct checks should display when reviewing a continuation and snapshot
    Given as a "<userType>" I have a valid "<operatorType>" "<licenceType>" licence
    When i change my continuation and review date on Internal
    And i generate a continuation
    And i have logged in to self serve as "<userType>"
    Then the correct checks should display on the continuation review details page and continuation snapshot
    Examples:
      | userType   | operatorType | licenceType            |
      | consultant | goods        | standard_national      |
      | admin      | goods        | standard_international |
      | admin      | goods        | restricted             |
      | consultant | public       | standard_national      |
      | admin      | public       | standard_international |
      | admin      | public       | restricted             |
      | consultant | public       | special_restricted     |