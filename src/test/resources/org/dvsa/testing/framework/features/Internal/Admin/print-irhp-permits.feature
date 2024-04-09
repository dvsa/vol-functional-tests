Feature: Admin can print IRHP Permits

  Scenario:
    Given I am on the Printers page
    When I navigate to Print IRHP Permits
    And I search for a Permit Type & Validity Date
    Then the permits data table should be displayed