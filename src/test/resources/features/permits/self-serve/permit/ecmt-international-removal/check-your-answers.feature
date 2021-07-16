@ecmt_removal @EXTERNAL @OLCS-24979a @olcs-27581 @eupa_regression
Feature: ECMT International Number Check your answers Page

  Background:
    Given I have a "goods" "standard_international" licence
    And I am on the VOL self-serve site
    And I am on ECMT Removal check your answers page

  Scenario: Verify the contents and functionality on ECMT International removals page is as per the ACs
    Then the page heading on the check your answers page is correct
    And the ECMT Removals check your answers page has reference number
    And the ECMT Removals application answers are displayed on the check your answers page
    When I save and return to overview
    Then I am on the ECMT removals permits overview page with check your answers section marked as complete
    And  I click the ECMT Removals Check your answers link on the overview page again
    When I click the back link
    Then I am on the ECMT removals permits overview page with check your answers section marked as complete
    When I click the ECMT Removals Check your answers link on the overview page again
    Then I check content and click save and continue on the Check Your Answers page
    Then I should be on the declaration page

  Scenario: Verify that clicking Change links on ECMT International removals Check Your Answers page, takes the user to the respective pages as per the ACs
    And   I choose to change the ECMT Removals Permits Eligibility  section
    And   I save and return to overview
    Then I should be on the overview page
    When  I click the ECMT Removals Check your answers link on the overview page again
    And  I choose to change the ECMT Removals Permits Cabotage section
    And   I save and return to overview
    Then I should be on the overview page
    When  I click the ECMT Removals Check your answers link on the overview page again
    And  I choose to change the ECMT Removals Permits Number of permits section
    Then  I should be on the ECMT international removal number of permits page