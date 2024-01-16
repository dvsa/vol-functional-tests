@WebDav
@WebDavUI
Feature: All WebDav functionality tests

  Background:
    Given i have a valid "goods" "standard_national" licence
    And i have logged in to internal
    And  i url search for my licence
    When i generate a letter

  Scenario: First time opening document (Ran first)
    And i open the document in word for the first time
    Then i should be prompted to login

  Scenario: Generate a letter using WebDav
    And i make changes to the document with WebDav and save it
    Then the document should contain the changes

  Scenario: Delete a generated letter
    And i save the letter
    And I delete generated letter above from the table
    Then the document should be deleted

  Scenario: Check link appears in generate letter pop up
    Then The pop up should contain letter details

  Scenario: Generate letter and check generation
    When i save the letter
    Then the "BUS_REG_CANCELLATION" document should be generated

  Scenario: Upload a document
    When i save the letter
    And upload a document
