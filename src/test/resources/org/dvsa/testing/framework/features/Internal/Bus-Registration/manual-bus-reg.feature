@MANUAL-BUS-REG
@int_regression
@CPMS_tests
@FullRegression
@printAndSign

Feature: Complete Manual Bus Registration Steps

  Background: Testing
    Given I have a psv application with traffic area "north_east" and enforcement area "north_east" which has been granted
    And i have logged in to internal

    @localsmoke @manualbus
  Scenario: Paying Fees to Complete Bus Registration Manually
    And i add a new bus registration
    When it has been paid and granted
    Then the bus registration should be granted
    And the traffic areas should be displayed on the service details page

  Scenario: Editing a bus registration of status Registered
    When i add a new bus registration
    And it has been paid and granted
    Then all Service Details fields should be editable
    And the edited Bus Registration details should be saved