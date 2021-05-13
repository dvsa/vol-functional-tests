@Deprecated
Feature: Annual ECMT APGG Euro5|Euro6 with Cross trade(3 options)application flow

  Background:
    Given I have a "goods" "standard_international" licence
    And   I am on the VOL self-serve site
    And   I am on the application overview page

  @EXTERNAL @annualECMTAPGG @OLCS-27819 @Deprecated
  Scenario: Verify ECMT Annual APGG application flow
    When I go back
    Then I am navigated back to the permits dashboard page with my application status shown as Not yet Submitted
    Then I submit the APGG application with cross trade
    Then I am navigated back to the permits dashboard page with my application status shown as Under Consideration
    And  I Grant the application on internal
    And  I accept and pay the issuing fee on Selfserve
    Then the application can be viewed in issued permits table