@EXTERNAL @ecmt_removal @eupa_regression
Feature: ECMT International Removal Cancel application Page

  Background:
    Given I have a "goods" "standard_national" licence
    And I am on the VOL self-serve site
    And I am on the ECMT International Removal overview page
    And I click cancel application link on the International removal overview page

  #AC01
  @OLCS-24814
  Scenario: Application back button in ECMT International Removal Cancel Page
    When I click the back link
    Then I should be on the overview page

  @OLCS-24814 @r222gremovalsfix  @OLCS-27781  @OLCS-28352
  Scenario: Cancel Application page details are displayed correctly
    Then I am navigated to the cancel application page
    And the licence number is displayed correctly
    And I am navigated to the cancel application page
    And the cancel application page displays the correct text
    And  the correct text is displayed next to the checkbox in ECMT Removal cancellation page
    When the ECMT International Removal cancel application button is selected without checkbox ticked
    Then I should get an error message
    When the checkbox is selected
    And  I select cancel application button
    Then I should be taken to cancel confirmation page
    When I select finish button
    Then I should be taken to the permits dashboard