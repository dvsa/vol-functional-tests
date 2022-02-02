@lgv
Feature: Goods SI and non Goods SI display the correct authorities on the internal interim pages

  Scenario: A goods international should display the hgv, lgv and trailer authorities
    Given I have a "goods" "standard_international" application
    And I submit the application with an interim
    When i view the application interim on internal
    Then hgv, lgv and trailer interim authorities should be present

  Scenario: A goods standard national should display a vehicle and trailer authorisation
    Given I have a "goods" "standard_national" application
    And I submit the application with an interim
    When i view the application interim on internal
    Then only vehicle and trailer interim authorisations should be present