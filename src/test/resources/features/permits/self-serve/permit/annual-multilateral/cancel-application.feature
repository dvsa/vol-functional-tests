#permit type is not being used at the moment
@MULTILATERAL
Feature: Cancel application

  Background:
    Given I have a "goods" "standard_international" licence
    And I am on the VOL self-serve site
    And I am on the cancel application page for Annual Multilateral

  #AC01
  @OLCS-23026
  Scenario: Application back button
    When I go back
    Then I should be on the Annual Multilateral overview page

  #AC02
  @OLCS-23026
  Scenario: Cancel application page functionality works as expected
    Then the annual multilateral Cancel Application page has a reference number
    And  the annual multilateral Cancel Application page has expected text
    And  the annual multilateral Cancel Application confirmation checkbox is unselected by default
    When I cancel my Annual Multilateral application without confirming
    Then I should get the expected error message for annual multilateral Cancel Application page
    When I confirm and cancel my annual multilateral permit
    Then I am taken to the application cancelled page
    And there are no fees for the permit