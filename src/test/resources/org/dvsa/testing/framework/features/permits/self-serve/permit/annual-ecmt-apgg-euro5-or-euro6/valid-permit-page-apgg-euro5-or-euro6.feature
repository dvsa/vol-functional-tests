@EXTERNAL @annual_ecmt_apgg_euro5_or_euro6
Feature: ECMT valid permit page

  Background:
    Given I have a "goods" "standard_international" licence
    And have valid permits

  @OLCS-21464 @OLCS-25083 @OLCS-28352
  Scenario: Has the correct information displayed
    Then the user is in the annual ECMT list page
    And the ECMT application licence number is displayed above the page heading
    And the ECMT permit list page table should display all relevant fields
    And I select return to permits dashboard hyperlink
    Then I should be taken to the permits dashboard