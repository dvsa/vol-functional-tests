Feature: Any references to trailers in safety and compliance texts are removed only for LGV Only variations

  Scenario: LGV Only safety and compliance page has no mention of trailers (Self serve) GB
    Given I have a valid "GB" lgv only licence
    And i create an lgv authorisation variation with 5 more LGVs
    When i navigate to the "variation" safety and compliance page
    Then there is no reference of trailers on the safety and compliance page

  Scenario: LGV Only safety and compliance page has no mention of trailers (Self serve) NI
    Given I have a valid "NI" lgv only licence
    And i create an lgv authorisation variation with 5 more LGVs
    When i navigate to the "variation" safety and compliance page
    Then there is no reference of trailers on the safety and compliance page

  Scenario: Mixed fleet safety and compliance page has the existing mention of trailers (Self serve)
    Given i have a valid "goods" "standard_international" licence
    And i create an lgv authorisation variation with 5 more LGVs
    When i navigate to the "variation" safety and compliance page
    Then there is trailer related information on the safety and compliance page

  Scenario: Other goods licences safety and compliance page has the existing mention of trailers
    Given i have a valid "goods" "standard_national" licence
    And i increase my vehicle authority count
    When i navigate to the "variation" safety and compliance page
    Then there is trailer related information on the safety and compliance page

  Scenario: PSV licences safety and compliance page has no mention of trailers
    Given i have a valid "public" "standard_international" licence
    And i increase my vehicle authority count
    When i navigate to the "variation" safety and compliance page
    Then there is no reference of trailers on the safety and compliance page

  Scenario: LGV Only safety and compliance page has no mention of trailers (Internal caseworkers)
    Given I have a valid "GB" lgv only licence
    And I create and save an lgv authorisation variation on internal with 5 more LGVs
    When i navigate to the "variation" safety and compliance page on internal
    Then there is no reference of trailers on the safety and compliance page

  Scenario: Mixed fleet safety and compliance page has the existing mention of trailers (Internal caseworkers)
    Given i have a valid "goods" "standard_international" licence
    And I create and save an lgv authorisation variation on internal with 5 more LGVs
    When i navigate to the "variation" safety and compliance page on internal
    Then there is trailer related information on the safety and compliance page