@lgv
Feature: Checking visibility of LGV Related information on the snapshot

  Scenario: Check for GB SI LGV Only display on LGV Only SI application and check declaration present on snapshot
    Given I have a "GB" lgv only application
    And i have logged in to self serve
    When i navigate to the snapshot on the review and declarations page
    Then the lgv choice and declaration confirmation are visible as "Yes" and "Confirmed"
    And i close and refocus the tab

  Scenario: Check for GB SN application and check LGV Only stuff not present on snapshot (Regression)
    Given I have a "goods" "standard_international" application
    And i have logged in to self serve
    When i navigate to the snapshot on the review and declarations page
    Then the lgv choice is marked as No and declaration is not present
    And i close and refocus the tab

  Scenario: Check for NI SI LGV Only display on LGV Only SI application and check declaration present on snapshot
    Given I have a "NI" lgv only application
    And i have logged in to self serve
    When i navigate to the snapshot on the review and declarations page
    Then the lgv choice and declaration confirmation are visible as "Yes" and "Confirmed"
    And i close and refocus the tab

  Scenario: Check for GB PSV SI application to not display any LGV Stuff on snapshot (Regression)
    Given I have a "public" "standard_international" application
    And i have logged in to self serve
    When i navigate to the snapshot on the review and declarations page
    Then the lgv related choices are not visible on the snapshot
    And i close and refocus the tab

  Scenario: Check for GB SN application to not display any LGV Stuff on snapshot (Regression)
    Given I have a "goods" "standard_national" application
    And i have logged in to self serve
    When i navigate to the snapshot on the review and declarations page
    Then the lgv related choices are not visible on the snapshot
    And i close and refocus the tab

  Scenario: Check Goods LGV Only for new title, operating centre not present and LGV: 1 instead of vehicles
    Given I have a "GB" lgv only application
    And i have logged in to self serve
    When i navigate to the snapshot on the review and declarations page
    Then the total number of vehicles title has changed to light goods vehicles
    And i close and refocus the tab

  Scenario: Check Goods SN for old stuff still present
    Given I have a "goods" "standard_national" application
    And i have logged in to self serve
    When i navigate to the snapshot on the review and declarations page
    Then the total number of vehicles title still displays vehicles and the operating centre is still present
    And i close and refocus the tab