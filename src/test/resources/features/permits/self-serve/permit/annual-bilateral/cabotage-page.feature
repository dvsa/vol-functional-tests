@Deprecated
Feature: Annual bilateral Cabotage Page

  Background:
    Given I have a "goods" "standard_international" licence
    And I am on the VOL self-serve site

  @EXTERNAL @OLCS-27071 @bilateral_cabotage_only @olcs-28201
    Scenario: Cabotage page application flow  for bilateral cabotage only option for multiple countries selection
    And  I am on the Bilateral Cabotage Page with more than one countries selected
    Then the page heading on the bilateral cabotage page is displayed correctly
    And  the advisory texts on the bilateral cabotage page are displayed correctly
    When select save and continue without confirming
    Then the cabotage relevant error message is displayed
    When I select 'no' button
    Then the relevant advisory text message is displayed
    When I select 'yes' button and save and continue
    Then the user is navigated to the next page

  @EXTERNAL @OLCS-27071 @bilateral_cabotage_only @OLCS-27781
  Scenario: Behaviour of save and continue button with no option for single country selection
    And  I am on the Bilateral Cabotage Page with norway selection
    When I select 'no' button
    Then the relevant advisory text message is displayed
    When I save and continue
    Then I am navigated to the cancel application page

  @EXTERNAL @OLCS-27071 @bilateral_cabotage_only @OLCS-27781
  Scenario: Behaviour of save and continue button with no option for multiple country selection
    And  I am on the Bilateral Cabotage Page with more than one countries selected
    When I select 'no' button
    Then the relevant advisory text message is displayed
    When I save and continue
    Then I m navigated to the correct page depending upon whether there is just one country selected or more than one

  @EXTERNAL @OLCS-27071 @BILATERALStandardAndCabotage
  Scenario: Cabotage page functionality for standard and cabotage permits application flow for multiple countries selection
    And  I am on the cabotage page for standard and cabotage permits with more than one countries selected
    Then the page heading on the bilateral cabotage page is displayed correctly
    And  the advisory texts on the bilateral standard and cabotage permits page are displayed correctly
    When select save and continue without confirming
    Then the cabotage relevant error message is displayed
    When I select 'yes' button and save and continue
    Then the cabotage relevant error message is displayed
    When I select 'no' button
    And I save and continue
    Then the user is navigated to the next page

  @EXTERNAL @OLCS-27071 @BILATERALStandardAndCabotage
  Scenario: Cabotage page functionality for standard and cabotage permits application flow for norway
    And  I am on the Bilateral Cabotage Page for standard and cabotage permits with norway selection
    Then the page heading on the bilateral cabotage page is displayed correctly
    And  the advisory texts on the bilateral standard and cabotage permits page are displayed correctly
    When select save and continue without confirming
    Then the cabotage relevant error message is displayed
    When I select 'yes' button and save and continue
    Then the cabotage relevant error message is displayed
    When I select 'no' button
    And I save and continue
    Then the user is navigated to the next page

  @EXTERNAL @OLCS-27071 @bilateral_standard_permits_no_cabotage
  Scenario: Cabotage page functionality for standard permits no cabotage application flow for norway
    And  I select standard permits no cabotage application
    Then  the cabotage page is not displayed in the application flow and taken to number of permits page
