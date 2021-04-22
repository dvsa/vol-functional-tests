@Deprecated
Feature: Bilateral Permit application submitted page

  Background:
    Given I have valid Goods standard_international VOL licence
    And I am on the VOL self-serve site


  @EXTERNAL @OLCS-22911 @OLCS-26045 @OLCS-26819 @OLCS-27361 @bilateral_cabotage_only
  Scenario: Annual Bilateral Cabotage Application has correct content and Finish button takes user to Permits Dashboard
    And  I'm on the annual bilateral cabotage only submitted  page
    Then my application reference should be displayed
    And  the submitted page advisory texts are displayed as per AC
    And  I should see the view receipt link
    When I select finish
    Then My application status changes to Valid

  @EXTERNAL @OLCS-27361 @bilateral_cabotage_only
  Scenario: Fee payments paid on Selfserve does not display the view receipt link
    And I have partial annual bilateral applications
    And I select the fee tab on the selfserve
    And I select the fee tab and pay the outstanding fees
    And I continue with the on-going Annual Bilateral application
    When I select the declaration link on the overview page
    And I submit my annual bilateral declaration
    And I am on the Annual Bilateral application submitted page
    Then I should not see the view receipt link

  @EXTERNAL @OLCS-22911 @bilateral_cabotage_only @OLCS-26045 @OLCS-27361
  Scenario: Fee waived, view receipt link is NOT displayed
    And I have partial annual bilateral applications
    And I am viewing a good operating licence on internal
    And all fees have been waived
    When I'm on the annual bilateral submitted page for my active application
    Then I should not see the view receipt link

  @EXTERNAL @OLCS-22911 @bilateral_cabotage_only @OLCS-26045 @OLCS-27361
  Scenario: Fee payments processed by case worker
    And I have partial annual bilateral applications
    And I am viewing a good operating licence on internal
    And pay outstanding fees
    When I'm on the annual bilateral submitted page for my active application
    Then I should not see the view receipt link

  @EXTERNAL  @OLCS-27361 @BILATERALStandardAndCabotage @olcs-27581
  Scenario: Annual Bilateral Standard and Cabotage Application has correct content and Finish button takes user to Permits Dashboard
    And  I'm on the annual bilateral StandardAndCabotagePermits only submitted page
    Then my application reference should be displayed
    And  the submitted page advisory texts are displayed as per AC
    And  I should see the view receipt link
    When I select finish
    Then My application status changes to Valid

  @EXTERNAL  @OLCS-27361 @bilateral_standard_permits_no_cabotage @olcs-27581
  Scenario: Annual Bilateral Standard permits No Cabotage Application has correct content and Finish button takes user to Permits Dashboard
    And I'm on the annual bilateral StandardPermitsNoCabotage only submitted page
    Then my application reference should be displayed
    And  the submitted page advisory texts are displayed as per AC
    And  I should see the view receipt link
    When I select finish
    Then I should be on the permits dashboard page with an ongoing application