@Deprecated
Feature: Short term APSG with sectors Percentage of International Journey

  Background:
    Given I have a "goods" "standard_international" licence
    And   I am on the VOL self-serve site

  @EXTERNAL @OLCS-25907 @OLCS-28226
  Scenario: Proportion of International Journey  details are displayed correctly
    When  I am on short term proportion of international journey page
    And   the page heading on short term international journey page is displayed correctly
    And   I save and continue
    Then  the error message should be displayed
    When  I select save and return overview link
    Then  the error message should be displayed
    When  I select proportion of International Journey and save and continue
    Then  I should be able to navigate to the next page

  @EXTERNAL @OLCS-25907
  Scenario: Verify  answers due to high intensity of use
    When  I am on short term proportion of international journey page
    And  I specify  high percentage of international journey
    Then the high intensity warning message is displayed