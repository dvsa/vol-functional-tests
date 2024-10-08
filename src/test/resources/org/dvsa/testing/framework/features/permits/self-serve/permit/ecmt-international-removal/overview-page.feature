@ecmt_removal @eupa_regression
Feature: ECMT International Removal  overview page

  Background:
    Given I have a "goods" "standard_international" licence
    And  I am on the ECMT International Removal overview page

  #AC01
  @EXTERNAL @OLCS-24817
  Scenario: Application back button
    When I click the back link
    Then the user is on self-serve permits dashboard

  #AC02:
  @EXTERNAL @OLCS-24817
  Scenario: Licence number is shown in overview page
    Then the licence number is displayed correctly

  #AC03,#AC04
  @EXTERNAL @OLCS-24817
  Scenario: Page Heading is displayed correctly
    Then the overview page heading is displayed correctly

  #AC09
  @EXTERNAL @OLCS-24817
  Scenario: Section links are disabled if previous steps have not been completed
    Then future sections beyond the current step are disabled