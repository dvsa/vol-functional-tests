@OLCS-17639
@INT
@int_regression
Feature: Interim License document generation

  Background:
    Given i have an interim "goods" "sn" application
    And i create admin and url search for my application

  Scenario: Documents automatically produced upon interim licence grant
    When the interim fee has been paid
    And the interim is granted
    Then the "GV Interim Licence" document should be generated