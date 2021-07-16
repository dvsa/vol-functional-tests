@Deprecated
Feature: Annual bilateral Period selection page

  Background:
    Given I have a "goods" "standard_international" licence
    And I am on the VOL self-serve site
    And I am on the Bilateral Period Selection page

  @EXTERNAL @OLCS-22906 @OLCS-26819 @OLCS-27069 @bilateral_cabotage_only
  Scenario: Verify Norway period selection page contents , country display , validation message functionality
    Then Country name displayed on the page is the one clicked on the overview page
    And  the page heading on bilateral period selection  page is correct
    When I select BilateralCabotagePermitsOnly period and click continue
    Then I am taken to the Bilateral permits use page

  @EXTERNAL @OLCS-27069 @bilateral_cabotage_only
  Scenario: Verify that correct error message is displayed if user clicks continue without selecting any period
    Then I get error message if there is more than one period available to select and I click continue without making any selection