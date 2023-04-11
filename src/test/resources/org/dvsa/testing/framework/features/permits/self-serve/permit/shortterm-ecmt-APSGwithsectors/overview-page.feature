@Deprecated
Feature: Short term Ecmt APSG with sectors overview page feature

  Background:
    Given I have a "goods" "standard_national" licence
    And I am on the VOL self-serve site
    And I am on short term ECMT overview Page

  @OLCS-25093  @olcs-27581
  Scenario: Page Heading and advisory texts are displayed
    And the overview page heading is displayed correctly
    #And the advisory texts on shortterm overview page are displayed correctly --- Advisory text removed from overview page
    #And there is a guidance on permits link -- Guidance link removed from overview page
    And the default section status are displayed as expected
    And future sections on shortterm overview page beyond the current step are disabled
    And I click the back link
    Then I should be taken to the permits dashboard