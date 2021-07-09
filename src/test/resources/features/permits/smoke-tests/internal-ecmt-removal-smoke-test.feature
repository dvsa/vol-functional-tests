@smoketest
Feature: ECMT removal application Internal End to End smoke test

  Background:
    Given I have a "goods" "standard_international" licence
    And i create an admin and url search for my licence

  @OLCS-28261
  Scenario: Case worker submits ECMT removal application successfully via Internal
    When the case worker submits partial ECMT Removal application
    And I pay fee for the ECMT removal application
    Then the application goes to valid status