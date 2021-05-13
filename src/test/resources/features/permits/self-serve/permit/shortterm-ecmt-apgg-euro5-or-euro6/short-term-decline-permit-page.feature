@EXTERNAL @shortterm_apgg_euro5_or_euro6
Feature: Short term APGG decline page permit page

  Background:
    Given I have a "goods" "standard_international" licence
    And I am on the VOL self-serve site
    And I have a short term application in awaiting fee status

  @OLCS-25666  @shortterm_apgg_euro5_or_euro6e2e @olcs-27581
  Scenario: Has the correct information displayed
    Then the user is taken to the awaiting fee page
    When I decline payment
    Then I should be on the short term decline awarded permits page
    And I should see all the relevant advisory texts
    And I select accept and continue button without confirming decline checkbox
    Then the error message is displayed
    And I select the decline confirmation checkbox and confirm
    Then I am taken to the permits declined page
    And I should see all the relevant texts on permits declined page
    And I select finish button
    Then I should be taken to the permits dashboard
    And the declined permit application is not displayed on the permit dashboard