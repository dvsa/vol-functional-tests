Feature: Email

  Background:
    Given I am on the VOL self-serve site

  @EXTERNAL @WIP
  Scenario: Receives confirmation of issued permits
    Given I have "2" "goods" "standard_international" licences
    And I am viewing a licence with an issued ECMT permit on internal
    When my issued permits are printed
    Then I should get an email notifying of the permits being issued