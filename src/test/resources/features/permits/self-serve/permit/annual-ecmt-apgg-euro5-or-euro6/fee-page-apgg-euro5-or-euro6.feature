@EXTERNAL @annual_ecmt_apgg_euro5_or_euro6 @eupa_regression
Feature: Annual ECMT Fee page

  Background:
    Given  I have valid Goods standard_international VOL licence
    And  I am on the VOL self-serve site
    And  I am on the fee page

  # TODO: Use backend as source for getting the reference number if dev's ever decide to document or inform others what the API is.
  @EXTERNAL @OLCS-21129 @ECMT @OLCS-24975 @Test2 @olcs-27502 @olcs-27581
  Scenario: Reference number matches what's on overview
    And the page heading and alert message on the fee page is displayed correctly
    And the table contents matches as per AC
    And I go back
    Then I should be on the Annual ECMT overview page

  @EXTERNAL @OLCS-21129 @ECMT @OLCS-24975 @annual_ecmt_apgg_euro5_or_euro6e2e @Test2 @olcs-27581
  Scenario: Successfully submits and pays
    When I submit and pay
    Then I should be taken to the next section

  @EXTERNAL @OLCS-21129 @ECMT @OLCS-24975 @Test2
  Scenario: Successfully returns to overview
    When I save and return to overview from fee page
    Then I should be on the Annual ECMT overview page