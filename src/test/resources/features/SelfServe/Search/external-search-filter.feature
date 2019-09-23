@SS
@External-Search-Filter
@ss_regression
@da_ss_regression
Feature: Using filters on External Search

  Background:
    Given i have a valid "goods" "sn" licence
    And i have searched for a licence

  Scenario: Check lorry and bus operator Organisation Type filter
    Then the Organisation Type filter should be displayed

  Scenario: Check lorry and bus operator Licence Type filter
    Then the Licence Type filter should be displayed

  Scenario: Check lorry and bus operator Licence Status filter
    Then the Licence Status filter should be displayed

  Scenario: Check lorry and bus operator Licence Traffic Area filter
    Then the Traffic Area filter should be displayed

  Scenario: Check lorry and bus operator Goods or PSV filter
    Then the Goods or PSV filter should be displayed