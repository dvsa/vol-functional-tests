@Deprecated
@EXTERNAL @shortterm_apsg_with_sectors
Feature: Short term ECMT APSG with sectors Cabotage Page

  Background:
    Given I have a "goods" "standard_international" licence
    And  I am on the VOL self-serve site
    And I am on the shortterm cabotage page

  @OLCS-25096 @OLCS-25483
  Scenario: Check that Short term permits Cabotage page  functionality and contents are as per the AC
    Then the page heading on the cabotage page is displayed correctly
    And the shortterm ecmt cabotage page has advisory messages
    And the short term ecmt cabotage page has application reference number
    When I save and continue
    Then I should get the cabotage page error message
    When I save and return to overview
    Then I should get the cabotage page error message
    When I click the back link
    Then I should be on the short term ECMT overview page

  Scenario: Save and continue after confirming cabotage navigates to next page
    When I confirm not undertaking cabotage journey
    Then the user is navigated to the next page

  @OLCS-25096
  Scenario: Save and return to overview after confirming cabotage updates and returns to overview page
    When I confirm not undertaking cabotage journey
    And I select save and return overview link
    Then the user is navigated to the overview page with the status as completed