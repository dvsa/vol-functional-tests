@Deprecated
Feature: Annual bilateral country selection page

  Background:
    Given I have valid Goods standard_international VOL licence
    And I am on the VOL self-serve site
    And I am applying for annual bilateral permit

  @EXTERNAL @OLCS-22906 @bilateral @OLCS-26819 @OLCS-27064
  Scenario: Verify Annual Bilateral country selection page contents , country display , validation message functionality
    Given I am on the country selection page
    Then bilateral country selection page licence reference number is correct
    And  the page heading on bilateral country selection  page is correct
    And countries are displayed in alphabetical order
    When I select save and continue button on select countries page
    Then the bilateral countries page should display its error message

  @EXTERNAL @OLCS-22906 @bilateral @OLCS-27064
  Scenario: Selecting a country and saving and continuing takes the user to Overview page
    Given I am on the country selection page
    When I select a country from the bilateral countries page
    And I select save and continue button on select countries page
    Then I should be on the bilateral overview page

  @EXTERNAL @OLCS-22906  @Deprecated
  Scenario: Save and return to overview goes to bilateral overview page
   Given I am on the country selection page
   And I select a country from the bilateral countries page
   When I save and return to overview
   Then I should be on the bilateral overview page

  @EXTERNAL @OLCS-22906  @Deprecated
  Scenario: Remembers answer when you change your answer
    Given I'm on the bilateral check your answers page
    When I choose to change the bilateral countries section
    Then I should be on the bilateral countries page
    And my previously selected countries should be remembered