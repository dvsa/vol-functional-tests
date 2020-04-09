@SS
@Surrender-Review-Discs-SN
@OLCS-22260

Feature: Review Discs and Documentation Page

  Background:
    Given i have a valid "goods" "standard_national" licence
    When i choose to surrender my licence
    And i am on the review discs and documentation page

  Scenario: Disc & Document  Details page
    And the correct licence details should be displayed
    Then the correct contact details should be displayed

  Scenario: Current Discs Details
    And the correct destroyed disc details should be displayed
    And the correct lost disc details should be displayed
    Then the correct stolen disc details should be displayed

  Scenario: Documentation Details
   And the correct operator details should be displayed