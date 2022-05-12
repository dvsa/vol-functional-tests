@VOL-3129
Feature: Login into self service

  Scenario: Special characters in login id
    Given I am on Selfserve homepage
    When I attempt to login with a username with special characters
      | RAIN+CLOUDS   |
      | Jigwel;l      |
      | Messi,Lionel  |
      | #BillyBailey1 |
      | Tom_Jerry     |
      | Daffy-Duck    |
    Then I should be authenticated
    And redirected to the dashboard

  Scenario: Email like login id’s
    Given I am on Selfserve homepage
    When I attempt to login with a username with an email format
      | a@b.com |
    Then I should be authenticated
    And redirected to the dashboard

  Scenario: Users with the same email address
    Given I am on Selfserve homepage
    When I attempt to login with users that share the same email address
      | .,waynerooney., |
    Then I should be authenticated
    And redirected to the dashboard