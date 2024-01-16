@Deprecated
Feature: Short term ECMT APSG with sectors submitted page

  Background:
    Given I have a "goods" "standard_international" licence
    And I am on the VOL self-serve site

  @OLCS-23019
  Scenario: Application submitted page details for short term ECMT APSG with sectors are displayed correctly
    And I am on the application submitted page
    Then the reference number on the short term ECMT submitted page  is as expected
    And all advisory texts on short term ECMT submitted page is displayed correctly
    When I select view receipt from short term application submitted page
    Then I open the receipt and it should open in a new window
    When I select finish button
    Then the user is on self-serve permits dashboard

  @OLCS-23019
  Scenario: Fee already paid so view receipt link is not displayed on short term ECMT
    And I have an ongoing short term ECMT with all fees paid
    Then there shouldn't be a view receipt link on the shortterm ECMT submitted page

  @OLCS-23019 @olcs-27502
  Scenario: Fee paid by internal case worker
    When a case worker worker pays all fees for my ongoing short term  permit application
    Then there shouldn't be a view receipt link on the shortterm ECMT submitted page

  @OLCS-23019
  Scenario: Fee waived by internal case worker
    When a case worker waives all fees for my ongoing short term permit application
    Then there shouldn't be a view receipt link on the shortterm ECMT submitted page