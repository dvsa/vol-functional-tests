@EXTERNAL @annual_ecmt_apgg_euro5_or_euro6 @eupa_regression
Feature: Annual ECMT Application Confirmation Page

  Background:
    Given I have valid Goods standard_international VOL licence
    And I am on the VOL self-serve site

  @EXTERNAL @OLCS-21130 @OLCS-25086 @ECMT @olcs-27382 @Test2 @olcs-27502 @olcs-27581
  Scenario: ECMT Application submitted page details are displayed correctly
    When I am on the Annual ECMT application submitted page
    Then the reference number on the annual ECMT submitted page  is as expected
    And all advisory texts on Annual ECMT submitted page is displayed correctly
    When I select view receipt from Annual ECMT application submitted page
    Then the view receipt of Annual ECMT hyperlink opens in a new window
    When I select finish button
    Then the user is on self-serve permits dashboard

  @EXTERNAL @OLCS-21130 @OLCS-25086 @ECMT @Test2 @olcs-27502 @olcs-27581
  Scenario: Annual Fee already paid so view receipt link is not displayed on annual ECMT
    And I have an ongoing Annual ECMT with all fees paid
    Then there shouldn't be a view receipt link on the Annual ECMT submitted page

  @EXTERNAL @OLCS-21130 @OLCS-25086 @ECMT @Test2
  Scenario: Annual Fee paid by internal case worker
    When a case worker worker pays all fees for my ongoing Annual ECMT permit application
    Then there shouldn't be a view receipt link on the Annual ECMT submitted page

  @EXTERNAL @OLCS-21130 @OLCS-25086 @ECMT @Test2
   Scenario: Annual Fee waived by internal case worker
    When a case worker waives all fees for my ongoing Annual ECMT permit application
    Then there shouldn't be a view receipt link on the Annual ECMT submitted page