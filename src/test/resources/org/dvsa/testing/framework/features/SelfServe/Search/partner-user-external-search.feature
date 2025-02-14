@OLCS-20956 @ap
@SS-PARTNER-USER-SEARCH
@FullRegression
@printAndSign
Feature: Partner user external search by Address, Business name, Licence Number and Person's name

  Background:
    Given i login as a partner user

  @ec2_smoke
  Scenario: Business name partner external search for lorry and bus operators
    When I search for a lorry and bus operator by "business","","ANNULAR LIMITED (MLH)","",""
    Then search results page should display operator names containing our "ANNULAR LIMITED (MLH)"

  @search-lorry-by-licence @ss_regression
  Scenario: Licence number partner external search for lorry and bus operators
    When I search for a lorry and bus operator by "licence","OC1057274","","",""
    Then search results page should only display our "OC1057274"