@PP-SMOKE
Feature: generate a letter
  @generate_prep_letter
  Scenario: Check generate letter pop and print letter
    Given I have a prep "internal" account
    And i url search for my licence "318365"
    When i generate a letter
    And The pop up should contain letter details
    Then The letter is sent by "printAndPost"