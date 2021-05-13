@Deprecated
Feature: Annual trips abroad page

  Background:
    Given I am on the VOL self-serve site

  @EXTERNAL @ECMT @olcs-27581 @Deprecated
  Scenario: Text to inform users to exclude NI journeys in their answer
    Given I have a "goods" "standard_international" licence
#    Look into above and fix if needed. Test is deprecated though so possibly ignorable.
    Given I am on the VOL self-serve site
    And  I am on the application overview page
    And I am on the annual trips abroad page
    Then I should see text informing me not to include NI journeys

  @EXTERNAL @ECMT @Deprecated
  Scenario: Fails validation when saving and continuing
    Given I have a "goods" "standard_international" licence
    Given I am on the VOL self-serve site
    And  I am on the application overview page
    And I am on the annual trips abroad page
    And I don't specify a value for annual trips
    When I save and continue
    Then I should get an error message
    When I save and return to overview
    Then I should get an error message
    And  I specify an invalid input
    When I save and continue
    Then I should get an error message
    When I save and return to overview
    Then I should get an error message

  @EXTERNAL @ECMT @Deprecated
  Scenario: Fails validation by not specifying a value when saving and continuing
    Given I have a "goods" "standard_international" licence
    Given I am on the VOL self-serve site
    And  I am on the application overview page
    And I am on the annual trips abroad page
    And I don't specify a value for annual trips
    When I save and continue
    Then I should get an error message
    And I specify a valid amount of annual trips
    When I save and return to overview
    Then I should be on the Annual ECMT overview page

  @Deprecated
  Scenario: Application back button
    Given I have a "goods" "standard_international" licence
    Given I am on the VOL self-serve site
    And  I am on the application overview page
    And I am on the annual trips abroad page
    And I don't specify an amount for annual trips
    When I go back
    Then I should be on the Annual ECMT overview page

  @OLCS-21460 @EXTERNAL @ECMT @WIP @Deprecated
  Scenario: Is informed that they may be asked to verify their answers due to high intensity of use
    Given I have a "goods" "standard_international" licence
    Given I am on the VOL self-serve site
    And I have a high intensity of use for number of permits
    Then I am informed that I may be asked to verify my answers on number of trips page