@lgv
Feature: Undertakings are generated on internal side when lgv only applications are submitted and paid

  Scenario: Undertaking is created on internal when a user submits and application
    Given I have a "GB" lgv only application
    And i have logged in to self serve
    And i navigate to the application review and declarations page
    When i submit and pay for the application
    Then the lgv only undertaking should be generated on internal matching relevant criteria

  Scenario: Undertaking is not created on internal when granting a Goods SI LGV Mixed licence
    Given i have a valid "goods" "standard_international" licence
    When I navigate to the undertakings page on internal
    Then an undertaking should not be generated on internal

  Scenario: Undertaking is created on internal when a LGV Only interim is granted by a caseworker
    Given I have a submitted "GB" lgv only application with interim
    And the lgv mixed interim is granted on internal
    When I navigate to the undertakings page on internal
    Then the lgv only undertaking should be generated on internal matching relevant criteria
