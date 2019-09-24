@INT
@int_regression
Feature: Grant under consideration application

  Scenario Outline:  Grant a licence
    Given I have a "<vehicleType>" "<typeOflicence>" application which is under consideration
    When I grant licence
    Then the licence should be granted
    And i create admin and url search for my application
    And the "<documentType>" document is produced automatically
    Examples:
      |vehicleType|typeOflicence|documentType|
      |goods|standard_international|GV Licence|
      |public|standard_national|PSV Licence|