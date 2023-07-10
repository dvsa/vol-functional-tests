@lgv-traffic-area
Feature: When setting the correspondence and establishment address, traffic areas should auto set

  Background:
    Given I create a new external user

  Scenario: When setting correspondence and establishment address on on lgv only NI application then traffic area should be NI
    And I apply for a "NI" "goods" "standard_international" "lgv_only_fleet" "checked" licence
    When i complete the application to and enter and save a north west correspondence address and an establishment address
    Then the traffic area is set as Northern Ireland

  Scenario: When setting only a correspondence address on lgv only NI application then traffic area should be NI
    And I apply for a "NI" "goods" "standard_international" "lgv_only_fleet" "checked" licence
    When i complete the application to and enter and save a north west correspondence address with no establishment address
    Then the traffic area is set as Northern Ireland

  Scenario: When setting correspondence and establishment address on lgv mixed NI application then traffic area is unset
    And I apply for a "NI" "goods" "standard_international" "mixed_fleet" "unchecked" licence
    When i complete the application to and enter and save a north west correspondence address with no establishment address
    Then the traffic area is not set as Northern Ireland

  Scenario: When adding an operating centre on lgv mixed NI application then traffic area is set as NI
    And I apply for a "NI" "goods" "standard_international" "mixed_fleet" "unchecked" licence
    And I add an operating centre with a northern ireland postcode
    And the traffic area is set as Northern Ireland

  Scenario: When setting correspondence and establishment address on an lgv only GB application then traffic area should be north west
    And I apply for a "GB" "goods" "standard_international" "lgv_only_fleet" "checked" licence
    When i complete the application to and enter and save a north west correspondence address and an establishment address
    Then the traffic area is set as North West of England

  Scenario: When setting only a correspondence address on on lgv only GB application then traffic area should be north west
    And I apply for a "GB" "goods" "standard_international" "lgv_only_fleet" "checked" licence
    When i complete the application to and enter and save a north west correspondence address with no establishment address
    Then the traffic area is set as North West of England

  Scenario: When setting correspondence and non-uk establishment address on an lgv only GB application then traffic area should be unset
    And I apply for a "GB" "goods" "standard_international" "lgv_only_fleet" "checked" licence
    When i complete the application to and enter and save a north west correspondence address and a non-uk establishment address
    Then the traffic area is not set as North West of England
    And the traffic area dropdown is available

  Scenario: When setting a non-uk correspondence address on an lgv only GB application then traffic area should be unset
    And I apply for a "GB" "goods" "standard_international" "lgv_only_fleet" "checked" licence
    When i complete the application to and enter and save a non-uk correspondence address
    Then the traffic area is not set as North West of England
    And the traffic area dropdown is available

  Scenario: When setting a north west correspondence address on an lgv mixed GB application then traffic area should be set
    And I apply for a "GB" "goods" "standard_international" "mixed_fleet" "unchecked" licence
    When i complete the application to and enter and save a north west correspondence address with no establishment address
    Then the traffic area is not set as North West of England
    And the traffic area dropdown is not available