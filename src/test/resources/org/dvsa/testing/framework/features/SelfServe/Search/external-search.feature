@OLCS-20956 @SS-EXTERNAL-SEARCH @ss_regression
@FullRegression @printAndSign
Feature: External user search by Address, Business name, Licence Number and Person's name

  Background:
    Given i have a self serve account
    And I am on the external search page


  Scenario: Address external user search for lorry and bus operators
    When I search for a lorry and bus operator by "address","","","","RUISLIP MANOR, GU21 2EP"
    Then search results page addresses "RUISLIP MANOR, GU21 2EP" should only display address belonging to our licence "OB1134621"

  Scenario: Business name external search for lorry and bus operators
    When I search for a lorry and bus operator by "business","","ASTUTE SOFTWARE DEVELOPMENT LIMITED","",""
    Then search results page should display operator names containing our "ASTUTE SOFTWARE DEVELOPMENT LIMITED"
    And search results page should only display our "OB1134621"

  Scenario: Licence number external search for lorry and bus operators
    When I search for a lorry and bus operator by "licence","OB1134621","","",""
    Then search results page should only display our "OB1134621"
    And search results page should only display our "OB1134621"

  Scenario: Person's name external search for lorry and bus operators
    When I search for a lorry and bus operator by "person","","","John Doe",""
    Then search results page should display the name "John Doe"