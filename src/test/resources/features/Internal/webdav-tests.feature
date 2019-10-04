@INT
@WebDav1
@int_regression
Feature: All WebDav functionality tests

  Background:
    Given i have a valid "goods" "sn" licence
    And i have logged in to internal
    And i url search for my licence

#  Scenario: First time opening document
#    When I generate a "edited" letter
#    And i open the document in word for the first time
#    Then i should be prompted to login
#
#  Scenario: Open a document for a user that has already logged in within the last hour
#    Then I generate a "edited" letter
#    And i write to a file the necessary information
#    When i navigate to the site to generate another new document
#    Then i should not be prompted to login

  Scenario: Generate a letter using WebDav
    Then i generate a letter
    And i write to a file the necessary information
#
#  Scenario: Check an already generated letter
#    Then I check the document has been generated
#    And I check the change has been made to the document