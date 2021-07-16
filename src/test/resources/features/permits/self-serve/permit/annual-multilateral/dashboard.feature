#permit type is not being used at the moment
@MULTILATERAL
Feature: Dashboard

  Background:
    Given I am on the VOL self-serve site
    Given I have a "goods" "standard_international" licence

  #AC02
  @OLCS-24115
  Scenario: Permits with status 'Not Yet Submitted'  are displayed in ongoing permit table
    And I have an ongoing Annual Multilateral Application
    And my annual multilateral permit has 'Not Yet Submitted' status
    Then my annual multilateral permit should be under the ongoing permit application table

  #AC03
  @OLCS-24115
  Scenario: Reference number is a link to permit application overview page
    And I have an ongoing Annual Multilateral Application
    And my annual multilateral permit has 'Not Yet Submitted' status
    When I select my annual multilateral permit application from external dashboard

  #AC04
  @OLCS-24115
  Scenario: Has expected ordering
    And I have an ongoing Annual Multilateral Application
    Then ongoing permits should be sorted by reference number in descending order