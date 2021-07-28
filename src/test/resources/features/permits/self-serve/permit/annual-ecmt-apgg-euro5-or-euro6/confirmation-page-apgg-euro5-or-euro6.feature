@EXTERNAL @ECMT @annual_ecmt_apgg_euro5_or_euro6 @eupa_regression
Feature: Annual ECMT Application Confirmation Page

  Background:
    Given I have a "goods" "standard_international" licence
    And I am on the VOL self-serve site

  @OLCS-21130 @OLCS-25086 @olcs-27382 @olcs-27502 @olcs-27581
  Scenario: ECMT Application submitted page details are displayed correctly
    When I am on the Annual ECMT application submitted page
    Then the reference number on the annual ECMT submitted page  is as expected
    And all advisory texts on Annual ECMT submitted page is displayed correctly
    Then I open the receipt and it should open in a new window
    When I select finish button
    Then the user is on self-serve permits dashboard

  @OLCS-21130 @OLCS-25086 @olcs-27502 @olcs-27581
  Scenario: Annual Fee already paid so view receipt link is not displayed on annual ECMT
    And I have an ongoing Annual ECMT with all fees paid
    Then there shouldn't be a view receipt link on the Annual ECMT submitted page

  @OLCS-21130 @OLCS-25086
  Scenario: Annual Fee paid by internal case worker
    Then there shouldn't be a view receipt link on the Annual ECMT submitted page

  @OLCS-21130 @OLCS-25086
   Scenario: Annual Fee waived by internal case worker
    Then there shouldn't be a view receipt link on the Annual ECMT submitted page