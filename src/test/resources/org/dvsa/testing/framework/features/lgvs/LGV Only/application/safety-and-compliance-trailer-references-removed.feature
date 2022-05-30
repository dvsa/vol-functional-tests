@lgv
Feature: Any references to trailers in safety and compliance texts are removed only for LGV Only applications

  Scenario: LGV Only safety and compliance page has no mention of trailers (Self serve) GB
    Given I have a "GB" lgv only application
    When i navigate to the "application" safety and compliance page
    Then there is no reference of trailers on the safety and compliance page

  Scenario: LGV Only safety and compliance page has no mention of trailers (Self serve) NI
    Given I have a "NI" lgv only application
    When i navigate to the "application" safety and compliance page
    Then there is no reference of trailers on the safety and compliance page

  Scenario: Mixed fleet safety and compliance page has the existing mention of trailers (Self serve)
    Given I have a "goods" "standard_international" application
    When i navigate to the "application" safety and compliance page
    Then there is trailer related information on the safety and compliance page

  Scenario: Other goods licences safety and compliance page has the existing mention of trailers
    Given I have a "goods" "standard_national" application
    When i navigate to the "application" safety and compliance page
    Then there is trailer related information on the safety and compliance page

  Scenario: PSV licences safety and compliance page has no mention of trailers
    Given I have a "public" "standard_international" application
    When i navigate to the "application" safety and compliance page
    Then there is no reference of trailers on the safety and compliance page

  Scenario: LGV Only safety and compliance page has no mention of trailers (Internal caseworkers)
    Given I have a "GB" lgv only application
    When i navigate to the "application" safety and compliance page on internal
    Then there is no reference of trailers on the safety and compliance page

  Scenario: Mixed fleet safety and compliance page has the existing mention of trailers (Internal caseworkers)
    Given I have a "goods" "standard_international" application
    When i navigate to the "application" safety and compliance page on internal
    Then there is trailer related information on the safety and compliance page