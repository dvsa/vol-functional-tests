@WIP
Feature: Username/password reset from login page

  Background:
    Given i have a valid "goods" "standard_national" licence
    When I am on Selfserve homepage

  Scenario: Forgotten password reset from login page
    And I have forgotten my password and want to reset
    Then I will be sent an email with my password


    Scenario: Forgotten username reset from login page
      And I have forgotten my username and want it to be sent
      Then I will be sent an email with my username




