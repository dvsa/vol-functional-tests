@Deprecated
Feature: ShortTerm ECMT Select Year Page

  Background:
    Given I have a "goods" "standard_international" licence
    And  I am on the VOL self-serve site
    And I am on select a year page

  @OLCS-25089
  Scenario: Select year page messages are  displayed Correctly
    Then I Should see select year page message displayed correctly
    And  I Should see warning displayed correctly
    And  I Should see one or more years to select to display correctly