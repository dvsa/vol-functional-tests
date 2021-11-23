Feature: Generate scanning separator sheets as an internal user

  Background:
    Given i have a valid "goods" "standard_national" licence
    When I have logged into the internal application

    Scenario: User wishes to create a scanning separator
      Given I am on the Scanning page
      When I select the Compliance Scanning Category
