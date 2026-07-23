@ss_accessibility
Feature: Self serve pages should comply to the WCAG 2.1 AA accessibility standards

  # ---------- Create application dashboard ----------

  @FullRegression
  Scenario: Check 'Not started' section colour
    Given I create a new external user
    And I apply for a "GB" "goods" "standard_international" "lgv_only_fleet" "checked" licence
    Then the colour of the 'Business type' section should be blue

  @FullRegression @directors-section
  Scenario: Check 'Can't start yet' section colour
    Given I create a new external user
    And I apply for a "GB" "goods" "standard_international" "lgv_only_fleet" "checked" licence
    Then the colour of the 'Directors' section should be grey

  @FullRegression
  Scenario: Scan for accessibility violations on the create application dashboard
    Given I create a new external user
    And I submit and pay for a "Goods" licence application
    And i scan for accessibility violations
    Then no issues should be present on the page

  # ---------- Messaging journey ----------

  @ss-message-accessibility
  Scenario Outline: Validate the messaging page for accessibility compliance
    Given as a "<user_type>" I have a valid "<Operator>" "<licence_type>" licence
    And i create an admin and url search for my licence
    And i have logged in to self serve as "<user_type>"
    When i click the messages heading
    Then i click on start a new conversation link and select the licence number
    Then colour of the "Send message" button should be green
    And i click Send message to send a message to the caseworker
    And i scan for accessibility violations
    Then no issues should be present on the page
    Examples:
      | user_type | Operator | licence_type |
      | admin     | goods    | restricted   |

  # ---------- EBSR journey ----------

  @FullRegression
  Scenario: Scan for accessibility violations on the EBSR journey
    Given as a "admin" I have a psv application with traffic area "west" and enforcement area "west" which has been granted
    When I upload an ebsr file with "41" days notice
    Then A short notice flag should be displayed in selfserve
    And i scan for accessibility violations
    Then no issues should be present on the page

  # ---------- Manage users ----------

  @VOL-273 @ss_regression @FullRegression
  Scenario: Scan for accessibility violations on manage users page
    Given i have an admin account to add users
    And i navigate to the manage users page
    When i scan for accessibility violations
    Then no issues should be present on the page

  @VOL-273 @ss_regression @FullRegression
  Scenario: Check button name on manage users page
    Given i have an admin account to add users
    And i navigate to the manage users page
    Then name of button should be 'Add a user'

  @VOL-273 @ss_regression @FullRegression
  Scenario: Check button colour on manage users page
    Given i have an admin account to add users
    And i navigate to the manage users page
    Then colour of the 'Add a user' button should be green

  @VOL-273 @ss_regression @FullRegression
  Scenario: Check column name on manage users page
    Given i have an admin account to add users
    And i navigate to the manage users page
    When i add a user
    Then remove button column should be named 'Remove'

  @VOL-273 @ss_regression @FullRegression
  Scenario: Check current users text and number on manage users page
    Given i have an admin account to add users
    And i navigate to the manage users page
    When i add a user
    Then user text should displaying current users

  # ---------- Surrender journey ----------

  @user-check-accessibility
  Scenario Outline: Scan for accessibility violations on surrender completion
    Given as a "<user_type>" I have a valid "goods" "standard_national" licence
    And my application to surrender is under consideration
    When the caseworker approves the surrender
    Then the licence status should be "Surrendered"
    And the surrender menu should be hidden in internal
    And the licence should not displayed in selfserve
    And i scan for accessibility violations
    Then no issues should be present on the page
    Examples:
      | user_type  |
      | admin      |
      | consultant |

  # ---------- Manage vehicle licence ----------

  Scenario: Manage Vehicle page is accessible
    Given I have "2" "goods" "standard_national" licences with "3" vehicles and a vehicleAuthority of "6"
    When I navigate to manage vehicle page on a licence
    And i scan for accessibility violations
    Then no issues should be present on the page

  Scenario Outline: Check Add Vehicle page is accessible
    Given I have "2" "goods" "standard_national" licences with "3" vehicles and a vehicleAuthority of "6"
    When I navigate to manage vehicle page on a licence
    And choose to add a "<VRM>" vehicle
    And i scan for accessibility violations
    Then no issues should be present on the page
    Examples:
      | VRM     |
      | PX57DXA |

  Scenario: Remove vehicle journey
    Given I have "2" "goods" "standard_national" licences with "3" vehicles and a vehicleAuthority of "6"
    When I navigate to manage vehicle page on a licence
    And I choose to remove a vehicle
    And i scan for accessibility violations
    Then no issues should be present on the page

  Scenario: Confirm vehicle removal
    Given I have "2" "goods" "standard_national" licences with "3" vehicles and a vehicleAuthority of "6"
    When I navigate to manage vehicle page on a licence
    And I want to confirm a vehicle removal
    And i scan for accessibility violations
    Then no issues should be present on the page

  Scenario: Reprint vehicle discs
    Given I have "2" "goods" "standard_national" licences with "3" vehicles and a vehicleAuthority of "6"
    When I navigate to manage vehicle page on a licence
    And I choose to reprint a vehicle disc
    And i scan for accessibility violations
    Then no issues should be present on the page

  Scenario: Confirm reprint vehicle discs
    Given I have "2" "goods" "standard_national" licences with "3" vehicles and a vehicleAuthority of "6"
    When I navigate to manage vehicle page on a licence
    And I want to confirm a vehicle disc reprint
    And i scan for accessibility violations
    Then no issues should be present on the page

  Scenario: Transfer vehicle journey
    Given I have "2" "goods" "standard_national" licences with "3" vehicles and a vehicleAuthority of "6"
    When I navigate to manage vehicle page on a licence
    And I choose to transfer a vehicle
    And i scan for accessibility violations
    Then no issues should be present on the page

  Scenario: Confirm transfer vehicle
    Given I have "2" "goods" "standard_national" licences with "3" vehicles and a vehicleAuthority of "6"
    When I navigate to manage vehicle page on a licence
    And I want to confirm a vehicle transfer
    And i scan for accessibility violations
    Then no issues should be present on the page
