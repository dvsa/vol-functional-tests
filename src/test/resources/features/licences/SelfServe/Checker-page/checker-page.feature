@ss_regression
@checker_page


Feature: User should be able to view the checker page before registering as a Self Service user

  Scenario: SS user can view checker page while logged in
    Given i have a valid "public" "standard_national" licence
    When I am on the checker page
    And I click Continue on the checker page
    Then I should be on the dashboard

  Scenario: SS user can view checker page not logged in
    Given redirected to the dashboard
    And I am on the checker page
    When I click Continue on the checker page
    Then I should be on the Self Serve login page