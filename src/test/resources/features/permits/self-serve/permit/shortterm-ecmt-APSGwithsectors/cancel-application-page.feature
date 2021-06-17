@Deprecated
Feature: Short term ECMT APSG with sectors Cancel application Page

  Background:
    Given I have a "goods" "standard_international" licence
    And I am on the VOL self-serve site
    And I am on short term ECMT overview Page
    And I click cancel application link on the short term ECMT overview page

  @EXTERNAL @OLCS-25131
  Scenario: Application back button in short term ECMT Page
    And I go back
    Then I should be taken back to short Term Overview Page

  @EXTERNAL @OLCS-25131 @OLCS-28226
  Scenario: Cancel application page details are displayed correctly
    Then I am navigated to the cancel application page
    And the cancel application page displays the correct text
    And I select cancel application button
    Then I should get an error message on cancel application page
    When the checkbox is selected
    And  I select cancel application button
    Then I should be taken to cancel confirmation page
    And I select finish button
    Then I should be taken to the permits dashboard