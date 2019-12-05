@ss_regression
@continuations

Feature: Continuations journey through internal and self serve

  Background:
    Given i have a valid "goods" "sn" licence

  Scenario: Continue a licence that has expired
    Given i have logged in to internal
    When i change my continuation date and generate a continuation on internal
    And fill in my continuation details on self serve
    Then the continuation should be approved