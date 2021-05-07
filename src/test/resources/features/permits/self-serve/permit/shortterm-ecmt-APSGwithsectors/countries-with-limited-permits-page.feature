@Deprecated
Feature: Short term ECMT APSG with sectors countries with limited permits page

  Background:
    Given I have valid Goods standard_international VOL licence
    And I am on the VOL self-serve site

  @OLCS-25905 @OLCS-28226
  Scenario:Countries with limited permits page details  are displayed correctly
    When  I am on short term countries with limited permits page
    And the application reference number on countries with limited permits page is shown correctly
    And the page heading on short term  countries with limited countries page is Shown Correctly
    And the advisory text on short term countries with limited countries page is Shown Correctly
    When I select save and continue without confirming
    Then I should get the relevant error message
    When I select save and return to overview link without confirming
    Then I should get the relevant error message
    And I go back
    Then I should be on the short term ECMT overview page
    And  I select the countries with limited permits hyperlink
    Then I should be on the countries with limited permits page
    When I have selected some short term countries with limited permits and clicked save and continue
    Then I should be taken to the next section