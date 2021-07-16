@Deprecated
Feature: Shortterm ECMT APSG with sectors Fee page

  Background:
    Given I have a "goods" "standard_international" licence
    And I am on the VOL self-serve site
    And I am on shortterm ECMT fee page

  @OLCS-25669
  Scenario: Fee page details are displayed correctly
    And the page heading and alert message on the fee page is displayed correctly
    And the issuing fee per permit link opens in a new window
    And the table contents on short term Fee page is  as per AC

  @OLCS-21129 @OLCS-24975
  Scenario: Successfully submits and pays
    When I submit and pay
    Then I should be taken to the next section

  @OLCS-21129  @OLCS-24975
  Scenario: Successfully returns to overview
    When I save and return to overview from fee page