#permit type is not being used at the moment
@MULTILATERAL
Feature: Annual Multilateral  permits page checks

  Background:
    Given I have a "goods" "standard_international" licence
    And I am on the VOL self-serve site
    And I am viewing an issued annual multilateral permit on self-serve

  #AC01
  @OLCS-24423 @OLCS-26046 @Deprecated
  Scenario: View valid annual multilateral permits in the licence list page
    Then the user is in the annual multilateral list page
    And the licence number is displayed above the page heading
    And  the Multilateral permit list page table should display all relevant fields
    And I select return to permit dashboard hyperlink
    Then I should be taken to the permits dashboard

  #AC02
  @OLCS-24423  @Deprecated
  Scenario: Back button takes back to Permit dashboard
    When the user is in the annual multilateral list page
    And  I click the back link
    Then I should be taken to the permits dashboard