@Deprecated
Feature: Annual bilateral check your answers page

  Background:
    Given I have valid Goods standard_international VOL licence
    And I am on the VOL self-serve site
    And I am on the Bilateral check your answers page

  @EXTERNAL @OLCS-27074 @bilateral_cabotage_only
  Scenario: Verify Norway Permits check your answers page contents
    Then Country name displayed on the Bilateral check your answers page is the one clicked on the overview page
    And  the page heading on bilateral check your answers page is correct

  @EXTERNAL @OLCS-27074 @bilateral_cabotage_only @olcs-27581
  Scenario: Verify that the Bilateral check your answers page has got all the sections displaying data correctly
    Then I see four sections displayed on the table correctly
    And  Period type displayed on the check your answers page is the one I selected on the Period selection page
    And  Journey type displayed on the check your answers page is the one I selected on the Permits usage
    And  Value of do you need to carry out cabotage, will always be 'YES'
    And Value of How many permits you need, will be the one saved on the number of permits page
    When I click Confirm and return to overview
    Then the status of Answer questions for individual countries section for the selected country is set as complete

  @EXTERNAL @OLCS-27074 @OLCS-27319 @bilateral_cabotage_only @olcs-27581
  Scenario: Verify that the Bilateral check your answers page change link functionality when period type is changed to Bilateral and Standard permits
   When I click change against Period for which you need permits section
   Then I am navigated to the Bilateral period selection page
   And  I change period to be Bilateral and Standard permits on the period selection and continue to be on the check your answers page
   Then Period type displayed on the check your answers page is the one I selected on the Period selection page
   And Journey type displayed on the check your answers page is the one I selected on the Permits usage
   And Value of Do you need to carry out cabotage, will be as per the selection after changing the period selection to Bilaterals Standard and Cabotage permits
   And Value of How many permits you need, will be the one saved on the number of permits page

  @EXTERNAL @OLCS-27074 @OLCS-27319 @bilateral_cabotage_only
  Scenario: Verify that the Bilateral check your answers page change link functionality when period type is changed to Bilateral Standard permits no Cabotage permits
    When I click change against Period for which you need permits section
    Then I am navigated to the Bilateral period selection page
    And  I change period to be Bilateral Standard permits no Cabotage on the period selection and continue to be on the check your answers page
    Then Period type displayed on the check your answers page is the one I selected on the Period selection page
    And Journey type displayed on the check your answers page is the one I selected on the Permits usage
    And Do you need to carry out cabotage, will not be displayed if the period type is Bilaterals Standard permits no Cabotage
    And Value of How many permits you need, will be the one saved on the number of permits page for Bileterals Standard permits no Cabotage