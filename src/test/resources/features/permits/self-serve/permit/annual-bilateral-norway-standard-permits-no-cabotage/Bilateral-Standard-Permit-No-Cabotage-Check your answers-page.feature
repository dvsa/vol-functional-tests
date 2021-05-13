@bilateral_standard_permits_no_cabotage
Feature: Annual bilateral standard permits no cabotage- Check your answers page feature

  Background:
    Given I have a "goods" "standard_international" licence
    And I am on the VOL self-serve site
    And I am on the Bilateral Standard permits no Cabotage check your answers page

  @EXTERNAL @OLCS-27316 @olcs-27581 @olcs-28201
  Scenario: Verify Standard Permits no cabotage check your answers page contents
    Then Country name displayed on the Bilateral check your answers page is the one clicked on the overview page
    And  the page heading on bilateral check your answers page is correct
    Then I see three sections displayed on the table correctly

  @EXTERNAL @OLCS-27316
  Scenario: Verify that the Bilateral Standard Permits no cabotage check your answers page has got all the sections displaying data correctly
    And  Period type displayed on the check your answers page is the one I selected on the Period selection page
    And  Journey type displayed on the check your answers page is the one I selected on the Permits usage
    And  For bilateral standard permits no cabotage permit type,the value of how many permits you need, will be as per the ones saved on the number of permits page
    When I click Confirm and return to overview
    Then the status of Answer questions for individual countries section for the selected country is set as complete