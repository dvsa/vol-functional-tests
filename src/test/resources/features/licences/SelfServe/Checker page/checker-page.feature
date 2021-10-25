


Feature: User should be able to view the checker page before registering as a Self Service user

  Scenario: SS user can view checker page while logged in
    Given i have logged in to self serve
    When I am on the checker page
    And I click Continue on the checker page
    Then I should be on the dashboard