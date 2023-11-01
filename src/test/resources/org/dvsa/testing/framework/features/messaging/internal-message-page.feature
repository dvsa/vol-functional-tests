@messaging

  Feature: Viewing messages tab as an internal user

  Scenario: Viewing from a licence
    Given i have a valid "goods" "restricted" licence
    And i create an admin and url search for my licence
    Then the messaging tab should be displayed
    