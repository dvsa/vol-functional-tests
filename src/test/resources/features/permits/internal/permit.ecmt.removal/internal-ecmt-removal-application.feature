@ECMTREMOVALINTERNAL
Feature: Caseworker creates an ECMT removal application

  Background:
    Given I have valid Goods standard_international VOL licence
    And I am on the VOL internal site
    And I am viewing a good operating licence on internal

    @OLCS-28232
  Scenario: Case Worker submits and pays for ECMT removal Application by Card
    And the case worker apply for an ECMT Removal application
    When I'm viewing my submitted ECMT Removal application
    And I pay fee for the ECMT removal application
    Then the application goes to valid status

  @OLCS-28232
  Scenario: Case Worker submits and pays for ECMT removal Application by waiving the fee
    And the case worker apply for an ECMT Removal application
    When I'm viewing my submitted ECMT Removal application
    And all fees have been waived
    Then the application goes to valid status

