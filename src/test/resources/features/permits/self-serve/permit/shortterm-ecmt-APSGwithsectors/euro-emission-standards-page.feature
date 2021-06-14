@Deprecated
Feature: Short term ECMT APSG with sectors Euro emission standards page

  Background:
    Given I have a "goods" "standard_international" licence
    And I am on the VOL self-serve site
    And I am on the Short term euro emission standard page

  @OLCS-25593
  Scenario: Euro EmissionStandard Page heading, advisory text and reference number are displayed correctly
    Then the euro emissions  page has the relevant information
    Then the short term emissions page has got the correct advisory text
    Then the short term emissions page checkbox has the correct text and displayed unselected by default
    When I save and continue
    Then I should get the emissions  page error message
    When I save and return to overview
    Then I should get the emissions  page error message
    When I go back
    Then I should be on the short term ECMT overview page

   @OLCS-25593
    Scenario: Save and return to overview on Emissions page after confirmation takes back to overview page with status completed
      When I confirm the emissions standards checkbox
      And I select save and return overview link
      Then the user is navigated to the short term overview page with the status of emissions displayed as completed

  @OLCS-25593
    Scenario: Save and continue after confirming emission standards navigates to next page
      When I confirm the emissions standards checkbox
      And  I save and continue
      Then the page heading on short term ECMT Annual Trips Abroad page is displayed correctly