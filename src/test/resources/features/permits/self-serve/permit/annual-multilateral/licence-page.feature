#permit type is not being used at the moment
@MULTILATERAL
Feature: Multilateral licence page

  Background:
    And I am on the VOL self-serve site

  #AC01
  @OLCS-23972
  Scenario Outline: All Goods Vehicles are valid for multilateral permits
    Given I have a "goods" "<licence_type>" licence
    And I am on the Annual Multilateral (EU and EEA) licence page
    Then My licence should be an option to apply for multilateral permit

    Examples:
      | licence_type          |
      | standard_international|
      | standard_national     |
      | restricted            |

  #AC02
  @OLCS-23972
  Scenario: Taken to multilateral overview page if there are no ongoing multilateral permit applications on chosen licence
    Given I have a "goods" "standard_national" licence
    And I am on the Annual Multilateral (EU and EEA) licence page
    When I select the licence to apply for an annual multilateral permit with
    Then I should be on the Annual Multilateral overview page