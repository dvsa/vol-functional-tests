Feature: Check that manage vehicle journey is accessible

  Scenario Outline: Check manage vehicle is accessible
    Given I have applied for a "<Operator>" "<LicenceType>" licence
    When I navigate to manage vehicle page
    And i scan for accessibility violations
    Then no issues should be present on the page

    Examples:
      | Operator | LicenceType       |
      | goods    | standard_national |