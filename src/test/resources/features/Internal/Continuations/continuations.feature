@ss_regression
@continuations

Feature: Continuations journey through internal and self serve

  Background:
    Given i have a valid "goods" "sn" licence

  Scenario: Continue a licence that has expired
    And i have logged in to internal
    When i change my continuation date
    And i generate a continuation
    And fill in my continuation details on self serve
    Then the continuation should be approved and a snapshot generated on Internal