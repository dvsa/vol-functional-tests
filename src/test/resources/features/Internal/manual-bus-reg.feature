@INT
@MANUAL-BUS-REG
@int_regression
@CPMS_tests
Feature: Complete Manual Bus Registration Steps

  Background:
    Given I have a psv application with traffic area "B" and enforcement area "EA-B" which has been granted
    And i have logged in to internal

  Scenario: Paying Fees to Complete Bus Registration Manually
    And i add a new bus registration
    When it has been paid and granted
    Then the bus registration should be granted
    And the traffic areas should be displayed on the service details page