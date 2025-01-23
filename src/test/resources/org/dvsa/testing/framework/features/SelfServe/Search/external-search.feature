@OLCS-20956 @SS-EXTERNAL-SEARCH @ss_regression
@FullRegression @printAndSign
Feature: External user search by Address, Business name, Licence Number and Person's name

  Background:
    Given i have a self serve account
    And I am on the external search page


  Scenario: Address external user search for lorry and bus operators
    When I search for a lorry and bus operator by "address","","","","BIRMINGHAM, UB2 5BD"
    Then search results page addresses "RUISLIP MANOR, GU21 2EP" should only display address belonging to our licence "OC1057274"

  Scenario: Business name external search for lorry and bus operators
    When I search for a lorry and bus operator by "business","","ANNULAR LIMITED (MLH)","",""
    Then search results page should display operator names containing our "ANNULAR LIMITED (MLH)"

  Scenario: Licence number external search for lorry and bus operators
    When I search for a lorry and bus operator by "licence","OC1057274","","",""
    Then search results page should only display our "OC1057274"
    And search results page should only display our "OC1057274"

  Scenario: Person's name external search for lorry and bus operators
    When I search for a lorry and bus operator by "person","","","John Doe",""
    Then search results page should display the name "John Doe"