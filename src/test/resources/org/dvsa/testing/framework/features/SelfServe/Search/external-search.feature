@OLCS-20956 @SS-EXTERNAL-SEARCH @ss_regression
@FullRegression @printAndSign
Feature: External user search by Address, Business name, Licence Number and Person's name

  Background:
    Given I am on the external search page

  @address_search
  Scenario: Address external user search for lorry and bus operators
    When I search for a lorry and bus operator by "address","","","","OK1145697"
    Then search results page addresses should only display address belonging to our licence "OK1145697"

  Scenario: Business name external search for lorry and bus operators
    When I search for a lorry and bus operator by "business","","ANNULAR LIMITED (MLH)","",""
    Then search results page should display operator names containing our "ANNULAR LIMITED (MLH)"

  Scenario: Licence number external search for lorry and bus operators
    When I search for a lorry and bus operator by "licence","OC1057274","","",""
    Then search results page should only display our "OC1057274"

  Scenario: Person's name external search for lorry and bus operators
    When I search for a lorry and bus operator by "person","","","MICHAEL JUPP",""
    Then search results page should display the name "MICHAEL JUPP"