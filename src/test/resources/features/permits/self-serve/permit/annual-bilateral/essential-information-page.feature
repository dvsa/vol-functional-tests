@Deprecated
Feature: Annual bilateral essential information page

  Background:
    Given I have valid Goods standard_international VOL licence
    And I am on the VOL self-serve site
    And I'm on bilateral overview page
    And I click on Norway country link on the Application overview page

  @EXTERNAL @OLCS-22906 @BILATERAL @OLCS-26819 @OLCS-27068
  Scenario: Verify that Norway Essential information page contents , country display and validations are correct
    When I am on the Norway essential information page
    Then Country name displayed on the Bilateral permit essential information page is the one clicked on the overview page
    And  the page heading on Bilateral essential information  page is correct
    And  the page content on Bilateral essential information  page is correct
    And the GOV.UK link on Bilateral essential information  page is correct
    When I select continue button on the Bilateral essential information page
    Then I am navigated to Bilaterals period selection page