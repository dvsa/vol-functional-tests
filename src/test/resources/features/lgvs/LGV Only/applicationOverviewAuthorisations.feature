Feature: Authorisation updates display correctly on Internal for caseworkers

  Scenario: LGV Only authorisation updates correctly
    Given I have a submitted "GB" lgv only application
    When I navigate to the application overview
    Then the LGV Only authorisation should be correct on the application overview screen

  Scenario: PSV SI authorisation updates correctly
    Given I have a submitted "public" "standard_international" application
    When I navigate to the application overview
    Then the PSV Standard International authorisation should be correct on the application overview screen

  Scenario: Goods SN authorisation updates correctly
    Given I have a submitted "goods" "standard_national" application
    When I navigate to the application overview
    Then the Goods Standard National authorisation should be correct on the application overview screen
