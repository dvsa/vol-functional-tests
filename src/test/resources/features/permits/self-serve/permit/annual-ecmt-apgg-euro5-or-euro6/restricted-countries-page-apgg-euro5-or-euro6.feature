@EXTERNAL @annual_ecmt_apgg_euro5_or_euro6
Feature: Restricted countries page

  Background:
    Given I have valid Goods standard_international VOL licence
    And  I am on the VOL self-serve site
    And I am on the restricted countries page

  @EXTERNAL @OLCS-24822 @ECMT @Test3 @olcs-27581 @OLCS-28275
  Scenario: Back button
    When I go back
    Then I should be on the Annual ECMT overview page

   @EXTERNAL @OLCS-24822 @ECMT @Test3 @OLCS-28275
  Scenario:Application number and details are shown correctly
    And the application reference number is shown correctly
    And the page heading on Annual ECMT countries with limited countries page is Shown Correctly
    And the Advisory text on Annual ECMT countries with limited countries page is Shown Correctly
    And I do plan on delivering to a restricted country
    And don't specify which
    And I save and continue
    Then  I should get an error message
    And  I save and return to overview
    Then  I should get an error message
    When  I have selected some restricted countries
    And   I save and continue
    Then  I should be taken to the next section

  @EXTERNAL @OLCS-21120 @ECMT @Test3 @olcs-27581 @OLCS-28275
  Scenario: User does not plan on going to restricted countries
    Given I don't plan on delivering to a restricted country
    When I save and continue
    Then I should be taken to the next section

  @EXTERNAL @OLCS-21120 @ECMT @Test3 @OLCS-28275
  Scenario: Passes validation checks when returning to overview
    Given I don't plan on delivering to a restricted country
    When I save and return to overview
    Then I should be on the Annual ECMT overview page