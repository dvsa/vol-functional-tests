@OLCS-24464
@ss_regression

Feature: SS user ability to withdraw application removed

  Scenario: SS user ability to withdraw application removed for new
    Given I have applied for a "public" "restricted" licence
    And i have logged in to self serve
    And the withdraw application link is present
    And i have logged in to internal
    And i add a case
    And i have logged in to self serve
    Then the withdraw application link is not present


  Scenario: SS user ability to withdraw application removed for variation
    Given I have a "goods" "standard_international" application which is under consideration
    And i have logged in to self serve
    And the withdraw application link is present
    And i have logged in to internal
    And i add a case
    And i have logged in to self serve
    Then the withdraw application link is not present
