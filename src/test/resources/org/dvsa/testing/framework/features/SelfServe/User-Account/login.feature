@VOL-3129
Feature: Login into self service

  Background:
    Given I am on Selfserve homepage

  Scenario Outline: Special characters in login id
    When I attempt to login with a username "<Username>" with special characters
    Then I should be authenticated
    And redirected to the dashboard
    Examples:
      | Username      |
      | RAIN+CLOUDS   |
      | Jigwel;l      |
      | Messi,Lionel  |
      | #BillyBailey1 |
      | Tom_Jerry     |
      | Daffy-Duck    |
      | abc123+.-/    |
      | Banana12+_@   |
      | Apple,,,      |

  Scenario Outline: Email like login id’s
    When I attempt to login with a "<Username>" with an email format
    Then I should be authenticated
    And redirected to the dashboard
    Examples:
      | Username |
      | a@b.com  |

  Scenario Outline: Users with the same email address
    When I attempt to login with "<Users>" that share the same email address
    Then I should be authenticated
    And redirected to the dashboard
    Examples:
      | Users           |
      | .,waynerooney., |