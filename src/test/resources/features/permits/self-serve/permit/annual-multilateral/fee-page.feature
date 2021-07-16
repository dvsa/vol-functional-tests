#permit type is not being used at the moment
@MULTILATERAL
Feature: Annual multilateral permits fee page

  Background:
    Given I have a "goods" "standard_international" licence
    And I am on the VOL self-serve site
    And I am on the annual multilateral fee page

  #AC01
  @OLCS-23018
  Scenario: Application back button returns to annual multilateral overview page
    Then the application details are displayed in fee page table
    And  the Fee-breakdown sub-heading can be seen below the fee summary table
    And  my fee should be tiered as expected
    When I click the back link

  #AC05
  @OLCS-23018
  Scenario: Submit and pay
    When I submit and pay
    Then I am taken to CPMS payment page

  #AC07
  @OLCS-23018
  Scenario: Saves and returns to overview