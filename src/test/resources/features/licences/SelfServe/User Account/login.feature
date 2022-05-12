@VOL-3129
Feature: Login into self service

  Scenario: Dodgy characters in login id
    Given I am on Selfserve homepage
    When I attempt to login with a username with special characters
      | user |
      |      |
      |      |
      |      |
      |      |
      |      |
      |      |
      |      |
      |      |
      |      |
      |      |
    Then I should be authenticated
    And redirected to the dashboard

  Scenario: Email like login id’s
    Given I am on Selfserve homepage
    When I attempt to login with a username with an email format
      | user |
      |      |
      |      |
      |      |
      |      |
      |      |
      |      |
      |      |
      |      |
      |      |
      |      |
    Then I should be authenticated
    And redirected to the dashboard

  Scenario: Users with the same email address
    Given I am on Selfserve homepage
    When I attempt to login with users that share the same email address
      | user |
      |      |
      |      |
      |      |
      |      |
      |      |
      |      |
      |      |
      |      |
      |      |
      |      |
    Then I should be authenticated
    And redirected to the dashboard