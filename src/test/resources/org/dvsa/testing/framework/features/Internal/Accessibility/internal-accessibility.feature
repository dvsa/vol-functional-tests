@int_accessibility
Feature: Internal application pages should comply to the WCAG 2.1 AA accessibility standards

  Scenario: Scan for accessibility violations on the application overview
    Given I have a submitted "goods" "standard_national" application
    And I have logged into the internal application
    When I navigate directly to my application in internal
    And I navigate to the application overview
    And i scan for accessibility violations
    Then no issues should be present on the page

  Scenario: Scan for accessibility violations on the case details page
    Given i have a valid "goods" "standard_national" licence
    And I create a new case
    And I navigate to a case
    When i scan for accessibility violations
    Then no issues should be present on the page

  Scenario: Scan for accessibility violations on the internal licence search results
    Given I have a submitted "goods" "restricted" application
    When i search for and click on my licence
    Then the "Licence details" page should display
    And i scan for accessibility violations
    Then no issues should be present on the page

  Scenario: Scan for accessibility violations on the internal tasks page
    Given i have a valid "goods" "standard_national" licence
    When I have logged into the internal application
    And i scan for accessibility violations
    Then no issues should be present on the page

  Scenario: Scan for accessibility violations on the System Messages admin page
    Given I am on the System Messages page
    When i scan for accessibility violations
    Then no issues should be present on the page

  Scenario: Scan for accessibility violations on the Your Account admin page
    Given i have a valid "goods" "standard_national" licence
    When I am on the Your Account page
    And i scan for accessibility violations
    Then no issues should be present on the page

  Scenario: Scan for accessibility violations on the task allocation rules admin page
    Given I am on the task allocation rules page
    When i scan for accessibility violations
    Then no issues should be present on the page

  Scenario: Scan for accessibility violations on the create internal user page
    Given i have an admin account to add users
    And I have logged into the internal application
    When i scan for accessibility violations
    Then no issues should be present on the page

  Scenario: Scan for accessibility violations on the manual bus registration page
    Given as a "admin" I have a psv application with traffic area "north_east" and enforcement area "north_east" which has been granted
    And i have logged in to internal as "admin"
    When i scan for accessibility violations
    Then no issues should be present on the page

  Scenario: Scan for accessibility violations on the submission page
    Given I have a submission
    When i scan for accessibility violations
    Then no issues should be present on the page

  Scenario: Scan for accessibility violations on the internal TM admin page
    Given i have a valid "goods" "standard_national" licence
    And I have logged into the internal application
    And i navigate to the admin transport managers details page
    When i scan for accessibility violations
    Then no issues should be present on the page

  Scenario: Scan for accessibility violations on the internal surrender details page
    Given i am on the surrenders review contact details page
    When i scan for accessibility violations
    Then no issues should be present on the page
