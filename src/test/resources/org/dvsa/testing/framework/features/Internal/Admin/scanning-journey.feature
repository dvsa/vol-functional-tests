Feature: Generate scanning separator sheets as an internal user

  Background:
    Given i have a valid "public" "standard_national" licence with an open case and bus reg

    Scenario: User wishes to create a scanning separator
      Given I am on the Scanning page
      When I complete the Compliance Scanning details
      Then A scanning success message banner should be displayed
