@OLCS-17639
@INT

Feature: Interim License document generation

  Background:
    Given i have an interim "goods" "sn" application
    And i create and url search for my licence

  Scenario: Documents automatically produced upon interim licence grant
    When the interim fee has been paid
    And the interim is granted
    Then the "GV Interim Licence" document is produced automatically

