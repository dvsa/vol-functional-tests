@OLCS-24464
@ss_regression

Feature: SS user ability to withdraw application removed for new

  Scenario: Operator chooses to sign with verify
    Given I have applied for a "public" "restricted" licence
    And i have logged in to self serve
    And i navigate to the application page
    And the withdraw application link is present
    And i have logged in to internal
    When i search for my licence
    And i add a new public inquiry
    And i add and publish a hearing
    And i have logged in to self serve
    Then the withdraw application link is not present


  Scenario: SS user ability to withdraw application removed for variation
    Given I have a "goods" "standard_international" application which is under consideration

