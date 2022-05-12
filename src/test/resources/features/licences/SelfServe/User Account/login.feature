Feature: Login into self service

  Scenario: Dodgy characters in login id
    Given I am on Selfserve homepage
    When I attempt to login with a username with special characters
    Then I should be authenticated
    And redirected to the dashboard

  Scenario: Email like login id’s
    Given I am on Selfserve homepage
    When I attempt to login with a username with an email format
    Then I should be authenticated
    And redirected to the dashboard

  Scenario: Users with the same email address
    Given I am on Selfserve homepage
    When I attempt to login with users that share the same email address
    Then I should be authenticated
    And redirected to the dashboard