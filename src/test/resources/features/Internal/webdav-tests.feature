@INT
@WebDav
@int_regression
Feature: All WebDav functionality tests

  Background:
    Given i have a valid "goods" "sn" licence
    And i have logged in to internal
    And i url search for my licence

  Scenario: Generate a letter using WebDav
    When i generate a letter
    And i make changes to the document with WebDav and save it
    Then the document should contain the changes