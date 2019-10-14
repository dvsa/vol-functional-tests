@INT
@WebDav
@int_regression
Feature: All WebDav functionality tests

  Background:
    Given i have a valid "goods" "sn" licence
    And i have logged in to internal
    And i url search for my licence

  Scenario: Delete a Licence document
    When I generate Licence Document
    And I delete a licence document from table
    Then the document should be deleted

  Scenario: Delete a generated letter
    When I generate a letter
    And I delete generated letter above from the table
    Then the document should be deleted