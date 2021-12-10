@ss_regression
<<<<<<< HEAD
@checker_page
=======
@checker-page
>>>>>>> e5722942fe1704a979254a53b7120744e55bf11d

Feature: User should be able to view the checker page before registering as a Self Service user

  Background:
    Given i have a valid "public" "standard_national" licence

  Scenario: SS user can view checker page while logged in
    Given i have logged in to self serve
    When I am on the checker page
    And I click Continue on the checker page
    Then I should be on the dashboard

  Scenario: SS user can view checker page not logged in
    Given I am on the checker page
    When I click Continue on the checker page
    Then I should be on the Self Serve login page