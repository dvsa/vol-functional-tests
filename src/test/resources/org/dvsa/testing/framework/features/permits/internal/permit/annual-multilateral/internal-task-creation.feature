#This permit type is not being used at the moment
@internal_multilateral
Feature: Internal Task Creation Functionality

  Background:
    Given I have a "goods" "standard_international" licence
    And I am on the VOL self-serve site
    And I have a valid annual multilateral permit
    And I am on the VOL internal site

  @OLCS-26954
  Scenario: CaseWorker is able to view the task created
   And I am on the internal home page
   And I navigate to the corresponding task created against the licence
   Then I can view the created task