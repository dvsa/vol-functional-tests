@OLCS-24464
@ss_regression

Feature: SS user ability to withdraw application removed when open cases are present

  Scenario: SS user ability to not withdraw application removed for new application with cases
    Given I have a submitted "public" "restricted" application
    And on self serve the withdraw application link is present on "application"
    And i add a case in internal on the "application" page
    Then on self serve the withdraw application link is not present on "application"

  Scenario: SS user ability to not withdraw application removed for variation with cases
    Given i have a valid "goods" "standard_national" licence
    And i create a variation
    And on self serve the withdraw application link is present on "variation"
    And i add a case in internal on the "variation" page
    Then on self serve the withdraw application link is not present on "variation"