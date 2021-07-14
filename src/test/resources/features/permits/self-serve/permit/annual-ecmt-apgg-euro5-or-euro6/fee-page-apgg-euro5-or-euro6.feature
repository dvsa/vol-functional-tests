@EXTERNAL @ECMT @annual_ecmt_apgg_euro5_or_euro6 @eupa_regression
Feature: Annual ECMT Fee page

  Background:
    Given I have a "goods" "standard_international" licence
    And  I am on the VOL self-serve site
    And  I am on the fee page

  @OLCS-21129 @OLCS-24975 @olcs-27502 @olcs-27581
  Scenario: Reference number matches what's on overview
    And the page heading and alert message on the fee page is displayed correctly
    And the table contents matches as per AC
    And I click the back link
    Then I should be on the Annual ECMT overview page

  @OLCS-21129 @OLCS-24975
  Scenario: Successfully returns to overview
    When I save and return to overview from fee page
    Then I should be on the Annual ECMT overview page