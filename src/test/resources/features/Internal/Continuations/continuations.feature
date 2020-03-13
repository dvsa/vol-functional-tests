@ss_regression
@continuations

Feature: Continuations journey through internal and self serve

  Background:
    Given i have a valid "goods" "sn" licence
    And i have logged in to internal
    When i change my continuation date
    And i generate a continuation

  Scenario: Continue a licence that has expired
    And fill in my continuation details on self serve
    Then the continuation should be approved and a snapshot generated on Internal

  Scenario: The users of ss display when reviewing a continuation
    And i have logged in to self serve
    Then the users of ss should display on the continuation review details page