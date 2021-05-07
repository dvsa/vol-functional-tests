@Deprecated
Feature: Annual bilateral permit type selection page

  Background:
    Given I have valid Goods standard_international VOL licence
    And I am on the VOL self-serve site

  @EXTERNAL @OLCS-22906 @OLCS-26819 @27064 @olcs-27581
  Scenario: Verify Annual Bilateral permit type selection page contents , validation message and back button functionality
    When I am applying for annual bilateral permit
    And Help text under Bilateral permits is displayed correctly
    And I select Bilateral permit on permit type selection page and click continue
    Then I am navigated to Bilaterals licence selection page