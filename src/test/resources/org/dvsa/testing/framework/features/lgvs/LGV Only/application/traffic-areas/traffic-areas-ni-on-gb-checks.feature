@lgv-traffic-area
Feature: Relevant errors are triggered when entering a NI postcode for a GB licence

  Scenario: When entering a NI correspondence and no establishment address on a GB application, an error is triggered.
    Given I have a "GB" lgv only application
    When i enter and save a NI correspondence address with no establishment address
    Then an error explaining that a NI address has been assigned to a GB application is displayed
    
  Scenario: When entering a NI establishment address on a GB application, an error is triggered.
    Given I have a "GB" lgv only application
    When i enter and save a NI establishment address
    Then an error explaining that a NI address has been assigned to a GB application is displayed

  Scenario: When entering a NI correspondence on a SI mixed GB application, an error is not triggered. (Regression)
    Given I have a "goods" "standard_international" application
    When i enter and save a NI correspondence address with no establishment address
    Then an error explaining that a NI address has been assigned to a GB application is not displayed

  Scenario: When entering a NI correspondence on a Non-Goods-SI mixed GB application, an error is not triggered. (Regression)
    Given I have a "public" "standard_national" application
    When i enter and save a NI correspondence address with no establishment address
    Then an error explaining that a NI address has been assigned to a GB application is not displayed