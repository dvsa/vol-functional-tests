@EXTERNAL @ECMT @annual_ecmt_apgg_euro5_or_euro6
Feature: Restricted countries page

  Background:
    Given I have a "goods" "standard_international" licence
    And I am on the restricted countries page

  @OLCS-24822 @olcs-27581 @OLCS-28275
  Scenario: Back button
    When I click the back link
    Then I should be on the Annual ECMT overview page

  @OLCS-24822 @OLCS-28275
  Scenario:Application number and details are shown correctly
    And the application reference number is shown correctly
    And the page heading on Annual ECMT countries with limited countries page is Shown Correctly
    And the Advisory text on Annual ECMT countries with limited countries page is Shown Correctly
    And I do plan on delivering to a restricted country
    And I click save and continue
    Then I should get an error message
    And  I save and return to overview
    Then I should get an error message

  @OLCS-21120 @olcs-27581 @OLCS-28275
  Scenario: User does not plan on going to restricted countries
    Given I don't plan on delivering to a restricted country
    When I click save and continue
    Then I should be on the ECMT number of permits page

  @OLCS-21120 @OLCS-28275
  Scenario: Passes validation checks when returning to overview
    Given I don't plan on delivering to a restricted country
    When I save and return to overview
    Then I should be on the Annual ECMT overview page