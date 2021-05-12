@Deprecated
Feature: Short term APSG with sectors Certificate required page

  Background:
    Given  I have valid Goods standard_international VOL licence
    And  I am on the VOL self-serve site
    And I am on the shortterm certificates required page

  @OLCS-25099 @OLCS-25483 @olcs-27502
  Scenario: Page heading, advisory text and reference number are displayed correctly
    Then the certificates required page has the relevant information
    And I save and continue
    And I should get the certificates required page error message
    And I select save and return overview link
    And  I should get the certificates required page error message
    When I go back
    Then I should be on the short term ECMT overview page

  @OLCS-25099
  Scenario: Save and return after confirmation takes back to overview page with status completed
    When I confirm the Certificates Required checkbox
    And I select save and return overview link
    Then the user is navigated to the short term overview page with the status as completed