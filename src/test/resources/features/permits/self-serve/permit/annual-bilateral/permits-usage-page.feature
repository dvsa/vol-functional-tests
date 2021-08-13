@Deprecated
Feature: Annual bilateral Permits Usage page

  Background:
    Given I have a "goods" "standard_international" licence
    And I am on the VOL self-serve site
    And I am on the Bilateral Permit usage page

  @EXTERNAL @OLCS-22906 @OLCS-26819 @OLCS-27290 @bilateral_cabotage_only
  Scenario: Verify Norway Permits Usage page contents , journey type display and validation message functionality
    Then Country name displayed on the Permits Usage page is the one clicked on the overview page
    And  the page heading on bilateral permits usage  page is correct
    When I select a random Journey type and click continue
    Then I am taken to the Bilateral Cabotage page

  @EXTERNAL @OLCS-27290 @bilateral_cabotage_only
  Scenario: Verify that correct error message is displayed if user clicks continue without selecting any Journey type
    Then I get error message if there is more than one Journey types available to select and I click continue without making any selection