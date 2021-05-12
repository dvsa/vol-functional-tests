@Deprecated
Feature: Short term ECMT APSG with sectors Cancel application Page

  Background:
    Given I have valid Goods standard_national VOL licence
    And I am on the VOL self-serve site
    And I am on short term ECMT overview Page
    And I click cancel application link on the short term ECMT overview page

  @EXTERNAL @OLCS-25131
  Scenario: Application back button in short term ECMT Page
    And I go back
    Then I should be taken back to short Term Overview Page

  @EXTERNAL @OLCS-25131 @OLCS-28226
  Scenario: Cancel application page details are displayed correctly
    Then the short term ECMT CancelApplication page heading should be correct
    And the short term  CancelApplication page displays the correct advisory text
    And the correct text is displayed next to the checkbox in short term ECMT page
    And I select cancel application button
    Then I should get an error message on short term ECMT cancel application page
    When the checkbox is selected
    And  I select cancel application button
    Then I should be taken to cancel confirmation page
    And I see the advisory text as per the AC
    And I select finish button
    Then I should be taken to the permits dashboard