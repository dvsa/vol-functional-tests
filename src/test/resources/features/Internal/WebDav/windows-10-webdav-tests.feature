@INT
@WebDav
Feature: All WebDav functionality tests

  Background:
    Given i have a valid "goods" "sn" licence
    And i have logged in to internal
    When i update my operating system on internal to "Windows 10"
    And i url search for my licence

  Scenario: First time opening document (Ran first)
    When i generate a letter
    And i open the document in word for the first time
    Then i should be prompted to login

  Scenario: Generate a letter using WebDav
    Then i generate a letter
    And i make changes to the document with WebDav and save it
    Then the document should contain the changes

  Scenario: Delete a Licence document
    When I generate Licence Document
    And I delete a licence document from table
    Then the document should be deleted

  Scenario: Delete a generated letter
    When i generate a letter
    And i save the letter
    And I delete generated letter above from the table
    Then the document should be deleted