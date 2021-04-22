@annual_ecmt_apgg_euro5_or_euro6
Feature: Awaiting fee permit page

  Background:
    Given I have a valid Goods standard_international VOL licence
    And I am on the VOL self-serve site
    And I am viewing an application that's awaiting fees

  @EXTERNAL @OLCS-21462 @OLCS-28275
  Scenario: Accept and pay
    When I accept and pay the issuing fee
    Then I should be taken to the payment provider

  @EXTERNAL @OLCS-21462 @OLCS-28275
  Scenario: Decline payment
    When I decline payment
    Then I should be on the decline awarded permits page

  @EXTERNAL @OLCS-21462 @OLCS-28275
  Scenario: back button returns to permits dashboard
    When I go back
    Then I should be on the permits dashboard page

  @EXTERNAL @OLCS-21462 @OLCS-28275
  Scenario: Cancel and return to permits dashboard
    When I cancel and return to dashboard from issuing fee page
    Then I should be on the permits dashboard page