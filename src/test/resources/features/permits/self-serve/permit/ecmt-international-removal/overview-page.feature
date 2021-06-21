@ecmt_removal @eupa_regression
Feature: ECMT International Removal  overview page

  Background:
    Given I am on the VOL self-serve site
    And I have valid Goods standard_international VOL licence
    And  I am on the ECMT International Removal overview page

  #AC01
  @EXTERNAL @OLCS-24817
  Scenario: Application back button
    When I go back
    Then the user is on self-serve permits dashboard

  #AC02:
  @EXTERNAL @OLCS-24817
  Scenario: Application number is shown in overview page
    Then the application number is displayed correctly

  #AC03,#AC04
  @EXTERNAL @OLCS-24817
  Scenario: Page Heading is displayed correctly
    Then the page heading on ECMT International Removal is correct

  #AC05 - GUIDANCE LINK NO LONGER DISPLAYED ON OVERVIEW PAGE
  @EXTERNAL @OLCS-24817  @olcs-27581 @Deprecated
  #Scenario:  Has 'guidance on permits' link
    #Then there's a guidance link for permits

  #AC09
  @EXTERNAL @OLCS-24817
  Scenario: Section links are disabled if previous steps have not been completed
    Then future sections beyond the current step are disabled