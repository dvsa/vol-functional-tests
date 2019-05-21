@INT
@GRANT-GOODS-APP
Feature: Grant under consideration application

  Background:
    Given I have a "goods" "standard_international" application which is under consideration

  Scenario: Grant a goods standard international licence
    When I grant licence
    Then the licence should be granted
    And i create and url search for my application
    And the "GV Licence" document is produced automatically