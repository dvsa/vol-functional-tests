@ecmt_removal
Feature: ECMT International Removal submission page

  Background:
    Given  I have valid Goods standard_international VOL licence
    And    I am on the VOL self-serve site

  @EXTERNAL @OLCS-26739 @olcs-28201
  Scenario: Application submission page details are displayed correctly
    And   I am on the ECMT International removal submission page
    Then the page heading on the submission page is displayed correctly
    And  the application reference number is displayed correctly
    And  the texts on the submission page are displayed correctly
    And  the view receipt of ECMT International hyperlink opens in a new window
    When I select finish button
    Then the application is under issued permits table with status as valid

  @EXTERNAL @OLCS-26739
  Scenario: Fee waived, view receipt link is NOT displayed
    And I have partial ECMT international removal application
    And I am viewing a good operating licence on internal
    And all fees have been waived
    When I'm on the ECMT international submitted page for my active application
    Then I should not see the view receipt link

  @EXTERNAL @OLCS-26739
  Scenario: Fee payments processed by case worker
    And I have partial ECMT international removal application
    And I am viewing a good operating licence on internal
    And pay outstanding fees
    When I'm on the ECMT international submitted page for my active application
    Then I should not see the view receipt link

  @EXTERNAL @OLCS-26739
  Scenario: Fees paid through Fees Tab
    And I have partial ECMT international removal application
    And I navigate to permit dashboard page
    When  I select the fee tab and pay the outstanding fees
    And I go back
    When  I proceed with the application
    Then I should not see the view receipt link