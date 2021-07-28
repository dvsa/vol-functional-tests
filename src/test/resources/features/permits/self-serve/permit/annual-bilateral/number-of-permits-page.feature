@Deprecated
Feature: Annual Bilateral permit number-of-permits page

  Background:
    Given I have a "goods" "standard_international" licence
    And I am on the VOL self-serve site

  #AC01
  @EXTERNAL @OLCS-24516 @bilateralCabotageonly @OLCS-27072
  Scenario: Functionality of Bilateral Cabotage number of  permits page are displayed correctly
    And I am on the annual bilateral number of permit page
    Then the page heading and the advisory text are displayed correctly according to the selection
    When I save and continue
    Then the relevant error message for annual bilateral number of permits page is displayed
    When I enter zero as value in the number of permits fields
    And  I save and continue
    Then the relevant error message for annual bilateral number of permits page is displayed
    When I enter the number of bilateral permits required
    Then the user is navigated to the next page
    When I select the fee tab on the selfserve
    Then the outstanding fees are displayed properly

  @EXTERNAL @OLCS-24516 @bilateral_standard_permits_no_cabotage @OLCS-27072 @olcs-27581
  Scenario: Functionality of Bilateral Standard Permits No Cabotage number of permits page are displayed correctly
    And  I am on the annual bilateral number of permit page for bilateral standard permits no cabatoge path
    Then the page heading and the advisory text on standard permits no cabotage page are displayed correctly according to the selection
    When I save and continue
    Then the relevant error message for annual bilateral number of permits page is displayed
    When I enter zero as value in the number of permits fields
    And  I save and continue
    Then the relevant error message for annual bilateral number of permits page is displayed
    When I enter the number of bilateral permits required
    Then the user is navigated to the next page
    When I select the fee tab on the selfserve
    Then the outstanding fees are displayed properly

  @EXTERNAL @OLCS-24516 @BILATERALStandardAndCabotage @OLCS-27072
  Scenario: Functionality of Bilateral Standard and Cabotage permits for no cabotage declaration are displayed correctly
    And  I am on the annual bilateral number of permit page for bilateral standard and cabotage permits path
    Then the page heading and the advisory text on standard permits no cabotage page are displayed correctly according to the selection
    When I save and continue
    Then the relevant error message for annual bilateral number of permits page is displayed
    When I enter zero as value in the number of permits fields
    And  I save and continue
    Then the relevant error message for annual bilateral number of permits page is displayed
    When I enter the number of bilateral permits required
    Then the user is navigated to the next page
    When I select the fee tab on the selfserve
    Then the outstanding fees are displayed properly

  @EXTERNAL @OLCS-24516 @BILATERALStandardAndCabotage @OLCS-27072 @olcs-27581
  Scenario: Functionality of Bilateral Standard and Cabotage permits for yes cabotage declaration are displayed correctly
    And  I am on the annual bilateral number of permit page for bilateral standard and cabotage permits with cabotage confirmation path
    Then the page heading and the advisory text on standard and cabotage permits for cabotage only page are displayed correctly according to the selection
    When I save and continue
    Then the relevant error message for annual bilateral number of permits page is displayed
    When I enter zero as value in the number of permits fields
    And  I save and continue
    Then the relevant error message for annual bilateral number of permits page is displayed
    When I enter the number of bilateral permits required
    Then the user is navigated to the next page
    When I select the fee tab on the selfserve
    Then the outstanding fees are displayed properly