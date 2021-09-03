Feature: Username/password reset from login page

  Scenario: Forgotten password reset from login page
    Given i have a valid "goods" "standard_national" licence
    When I am on Selfserve homepage
    And I have forgotten my password and want to reset
    Then I will be sent an email





