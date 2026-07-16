Feature: Export CSV file

  @smoke
  Scenario: Export CSV File on Manage Vehicle Page
    Given I have a "goods" "standard_national" licence
    When I navigate to manage vehicle page on a licence
   And I view the vehicles on my licence
    Then I click the Export list link that responds
