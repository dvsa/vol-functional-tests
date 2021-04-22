@bilateral_standard_and_cabotage_permits
Feature: Annual bilateral standard and cabotage Check your answers page feature

  Background:
    Given I have valid Goods standard_international VOL licence
    And I am on the VOL self-serve site
    And I am on the Bilateral Standard and Cabotage permits check your answers page

  @EXTERNAL @OLCS-27316
  Scenario: Verify that the Bilateral Standard and Cabotage permits check your answers page has got all the sections displaying data correctly
    Then I see four sections displayed on the table correctly
    And  Period type displayed on the check your answers page is the one I selected on the Period selection page
    And  Journey type displayed on the check your answers page is the one I selected on the Permits usage

  @EXTERNAL @OLCS-27316
  Scenario: Verify that the Bilateral Standard and Cabotage permits check your answers page has got all the sections displaying data correctly
    And  Period type displayed on the check your answers page is the one I selected on the Period selection page
    And  Journey type displayed on the check your answers page is the one I selected on the Permits usage
    And  Value of do you need to carry out cabotage, will always be as per the value selected on the cabotage page
    And For Bilateral Standard and cabotage permits ,the Value of How many permits you need, will be as per the ones saved on the number of permits page
    When I click Confirm and return to overview
    Then the status of Answer questions for individual countries section for the selected country is set as complete