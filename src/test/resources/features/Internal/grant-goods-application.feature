@INT
@GRANT-GOODS-APP
@int_regression
Feature: Grant under consideration application

  Background:
    Given I have a "goods" "standard_international" application which is under consideration

  Scenario: Grant a goods standard international licence
    When I grant licence
    Then the licence should be granted
    And i create admin and url search for my application
    And the "GV Licence" document is produced automatically