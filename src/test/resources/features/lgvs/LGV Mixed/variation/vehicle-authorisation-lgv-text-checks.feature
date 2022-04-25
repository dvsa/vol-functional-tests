@lgv
Feature: LGV Mixed variation authorisation text

  Scenario: LGV hint text is visible on the page
    Given i have a valid "goods" "standard_international" licence
    When i am on the operating centre and authorisations page
    Then the lgv hint text is visible

  @smoketest
  Scenario: If zero LGVs on the licence, then displays "HGVs" on the authorisation page
    Given I create a new external user
    And i have a "goods" "standard_international" licence with a hgv authorisation of "5" lgv authorisation of "0" in traffic area "1"
    When i am on the operating centre and authorisations page
    Then the hgv and lgv authorisations text are present
    And the vehicle authorisation text is not present

  Scenario: If one or more LGVs on the licence, then displays "HGVs" on the authorisation page
    Given i set to have 5 lgvs on my licence
    And i have a valid "goods" "standard_international" licence
    When i am on the operating centre and authorisations page
    Then the hgv and lgv authorisations text are present
    And the vehicle authorisation text is not present

  Scenario: If one or more LGVs on the variation, then displays "HGVs" on the authorisation page
    Given i set to have 5 lgvs on my licence
    And i have a valid "goods" "standard_international" licence
    When i begin an operating centre and authorisation variation
    Then the hgv and lgv authorisations text are present
    And the vehicle authorisation text is not present

  Scenario: On a goods standard international variation, the hgv text is present on the operating centre
    Given i have a valid "goods" "standard_international" licence
    When i begin an operating centre and authorisation variation
    Then the operating centre table hgv text is present

  Scenario: On a goods standard national variation, the hgv text is not present on the operating centre
    Given i have a valid "goods" "standard_national" licence
    When i begin an operating centre and authorisation variation
    Then the operating centre table vehicle text is not present

