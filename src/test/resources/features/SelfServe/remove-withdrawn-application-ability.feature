@OLCS-24464
@ss_regression

Feature: SS user ability to withdraw application removed

  Scenario: SS user ability to withdraw application removed for new application
    Given I have applied for a "public" "restricted" licence
    And on self serve the withdraw application link is present on "application"
    And i add a case in internal on the "application" page
    Then on self serve the withdraw application link is not present on "application"

  Scenario: SS user ability to withdraw application removed for variation
    Given i have a valid "goods" "sn" licence
    And i create a variation
    And on self serve the withdraw application link is present on "variation"
    And i add a case in internal on the "variation" page
    Then on self serve the withdraw application link is not present on "variation"