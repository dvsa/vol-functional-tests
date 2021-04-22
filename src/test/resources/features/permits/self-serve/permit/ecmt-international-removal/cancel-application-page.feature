@EXTERNAL @ecmt_removal
Feature: ECMT International Removal Cancel application Page

  Background:
    Given I have valid Goods standard_national VOL licence
    And I am on the VOL self-serve site
    And I am on the ECMT International Removal overview page
    And I click cancel application link on the International removal overview page

  #AC01
  @OLCS-24814
  Scenario: Application back button in ECMT International Removal Cancel Page
    When I go back
    Then I should be on the ECMT International Overview Page

  @OLCS-24814 @r222gremovalsfix  @OLCS-27781  @OLCS-28352
  Scenario: Cancel Application page details are displayed correctly
    Then I am on the ECMT Removals cancel application page
    And  the ECMT International Removal application reference number should be displayed above the heading
    And  the ECMT international removal  CancelApplication heading should be correct
    And  the ECMT International Removal CancelApplication page displays the correct advisory text
    And  the correct text is displayed next to the checkbox in ECMT Removal cancellation page
    When the ECMT International Removal cancel application button is selected without checkbox ticked
    Then I should get an error message
    When the checkbox is selected
    And  I select cancel application button
    Then I should be taken to cancel confirmation page
    When I select finish button
    Then I should be taken to the permits dashboard