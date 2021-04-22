@Deprecated
Feature: Annual bilateral declaration page

  Background:
    Given I have valid Goods standard_international VOL licence
    And I am on the VOL self-serve site

  @EXTERNAL @OLCS-22910 @bilateral_cabotage_only @OLCS-26819 @OLCS-27317
  Scenario: Verify Annual Bilateral application Declaration page contents , validations and back button functionality
    And  I'm on the annual bilateral cabotage only declaration page
    Then the application reference number and advisory text are displayed correctly
    When I accept and continue
    Then I should get an error message
    When I save and return to overview
    Then I should get an error message
    When I go back
    Then I should be on the bilateral overview page

  @EXTERNAL @OLCS-22910 @bilateral_cabotage_only @OLCS-26045 @OLCS-27317
  Scenario: Taken to the permit fee page
    And  I'm on the annual bilateral cabotage only declaration page
    When I submit my annual bilateral declaration
    Then I should be on the permit fee page

  @EXTERNAL @OLCS-22910 @bilateral_cabotage_only @OLCS-26045 @OLCS-27317
  Scenario: Section becomes complete when users save and accept after declaring on overview page
    And  I'm on the annual bilateral cabotage only declaration page
    And  I make my declaration
    And I save and return to overview
    Then the status for the declaration section is complete

  @EXTERNAL  @bilateral_cabotage_only  @OLCS-27317
  Scenario: When fees is paid Declaration page confirmation navigates to submission page
    And  I'm on the annual bilateral cabotage only declaration page
    And I select the fee tab on the selfserve
    And I select the fee tab and pay the outstanding fees
    And I continue with the on-going Annual Bilateral application
    When I select the declaration link on the overview page
    And I submit my annual bilateral declaration
    Then I am on the Annual Bilateral application submitted page

  @EXTERNAL  @bilateral_cabotage_only  @OLCS-27317
  Scenario: When fees is waived Declaration page confirmation navigates to submission page
    And  I'm on the annual bilateral cabotage only declaration page
    And I'm viewing my saved annual bilateral application in internal
    And I am on the fee details page
    And all fees have been waived
    And I am on the VOL self-serve site
    And I am continuing on the on-going Annual Bilateral application
    When I select the declaration link on the overview page
    And I submit my annual bilateral declaration
    Then I am on the Annual Bilateral application submitted page

  @EXTERNAL  @bilateral_standard_permits_no_cabotage @OLCS-27317
  Scenario: Bilateral Standard Permit No Cabotage Flow Declaration Page functionality
    And  I'm on the annual bilateral StandardPermitsNoCabotage only declaration page
    When I submit my annual bilateral declaration
    Then I should be on the permit fee page

  @EXTERNAL  @BILATERALStandardAndCabotage @OLCS-27317 @olcs-27581
  Scenario: Bilateral Standard and cabotage permits Flow Declaration Page functionality
    And  I'm on the annual bilateral StandardAndCabotagePermits only declaration page
    When I submit my annual bilateral declaration
    Then I should be on the permit fee page