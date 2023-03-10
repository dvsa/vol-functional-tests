@EXTERNAL @ECMT @annual_ecmt_apgg_euro5_or_euro6
Feature: ECMT Permit Euro Emission Standard Page

  Background:
    Given I have a "goods" "standard_international" licence

  @OLC-20557,@OLCS-24818 @olcs-27581 @OLCS-28275
  Scenario: Successful navigation of back link
    And I am on the euro emission standard page
    When I click the back link
    Then should see the overview page without updating any changes

  @OLC-20557,@OLCS-24818 @olcs-27581 @OLCS-28275
  Scenario: Fails validation when saving and returning to overview
    And I am on the euro emission standard page
    Then the licence number is displayed correctly
    And the texts are displayed correctly
    When I click save and continue
    Then I should see the validation errors for euro 6 page
    When I save and return to overview
    Then I should get an error message
    And I confirm the emissions standards checkbox
    When I save and return to overview
    Then I should see the overview page with updated changes