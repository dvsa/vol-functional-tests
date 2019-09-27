@INT
@WebDav
@int_regression
Feature: All WebDav functionality tests

  Background:
    Given i have a valid "goods" "sn" licence
    And i have logged in to internal
    And i url search for my licence

  Scenario: Generate a letter using WebDav
    Then I generate a "not edited" letter
    And I download and check the change has been made to the document