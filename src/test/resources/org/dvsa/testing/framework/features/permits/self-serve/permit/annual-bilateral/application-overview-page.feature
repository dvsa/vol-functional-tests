@Deprecated
Feature: Annual Bilateral Application Overview Page

  Background:
    Given I have a "goods" "standard_international" licence
    And I am on the VOL self-serve site

  @EXTERNAL @OLCS-27065
  Scenario: To verify the overview functionalites works as expected
   And I'm on bilateral overview page
   And the overview page heading is displayed correctly
   And the status of select countries you need for hyper link is marked as complete
   When the hyperlink for select countries is selected
   Then I am navigated to the relevant page
   And  I select save and continue button on select countries page
   Then the overview page heading is displayed correctly

  @EXTERNAL @OLCS-27315
  Scenario: Countries selected are listed in alphabetical order
    And I'm on bilateral overview page with multiple countries selected
    Then I can see the countries selected is listed in alphabetical order

  @EXTERNAL @OLCS-27315
  Scenario: Edit country selection button takes the user back to select countries page
    And I'm on bilateral overview page with multiple countries selected
    When I select the edit countries button
    Then I'm navigated to countries selection page

  @EXTERNAL @OLCS-27315
  Scenario: Answer questions for individual countries is marked as complete
    And I have completed till check your answers page
    Then the status of answers questions for individual countries as complete
    When I select the declaration link on the overview page
    Then I should see the correct heading on the declaration page
    When I declare and save and return to overview
    Then I am taken to the overview page with the status as completed
    When I select submit and pay link
    Then I should be on the permit fee page