@OLCS-24464
@ss_regression

Feature: SS user ability to withdraw application removed

  Scenario: SS user ability to withdraw application removed for new
    Given I have applied for a "public" "restricted" licence
    And on self serve the withdraw application link is present
    And i add a case in internal on the "licence" page
    Then on self serve the withdraw application link is not present


  Scenario: SS user ability to withdraw application removed for variation
    Given i have a valid "goods" "sn" licence
    And i create a variation
    And on self serve the withdraw application link is present
    And i add a case in internal on the "variation" page
    Then on self serve the withdraw application link is not present