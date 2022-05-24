@lgv
@cross-browser
Feature: Operating Centre authorisation variations triggering fees

  Scenario: LGVs total authorisation increase on a goods standard international triggers a fee
    Given I have a "goods" "standard_international" licence
    When i create an operating centre variation with "5" hgvs and "5" lgvs
    And complete the financial evidence page
    Then the review and declaration page should display pay and submit
    And the variation fee should be triggered

  Scenario: HGVs total authorisation increase on a goods standard international triggers a fee
    Given I have a "goods" "standard_international" licence with 2 operating centres
    When i change my total vehicle authorities to "10" HGVs "same" LGVs and "same" trailers without changing the operating centres
    And complete the financial evidence page
    Then the review and declaration page should display pay and submit
    And the variation fee should be triggered

  Scenario: HGV operating centre vehicle increase on a goods standard international triggers a fee
    Given I have a "goods" "standard_international" licence
    When i create an operating centre variation with "8" hgvs and "0" lgvs
    And complete the financial evidence page
    Then the review and declaration page should display pay and submit
    And the variation fee should be triggered

  Scenario: Adding a new operating centre on a goods standard international triggers a fee
    Given I have a "goods" "standard_international" licence
    When i add an operating centre and increase the vehicle total authority
    And complete the financial evidence page
    Then the review and declaration page should display pay and submit
    And the variation fee should be triggered

  Scenario: Increasing HGVs at an operating centre and increasing the LGVs total authorisation on a goods standard international triggers a fee
    Given I have a "goods" "standard_international" licence
    When i create an operating centre variation with "8" hgvs and "5" lgvs
    And complete the financial evidence page
    Then the review and declaration page should display pay and submit
    And the variation fee should be triggered

  @lgv-smoke
  Scenario Outline: Adding an OC and increasing vehicles on an existing OC triggers a fee
    Given I have a "goods" "<licenceType>" licence
    When i add an operating centre and increase the vehicle total authority
    And increase the authority on an existing operating centre authorisation and update the total authorisations
    And complete the financial evidence page
    Then the review and declaration page should display pay and submit
    And the variation fee should be triggered

    Examples:
      | licenceType            |
      | standard_international |
      | standard_national      |

  Scenario: Increasing HGVs at an OC, increasing LGVs total authorisation and reverting the HGVs increase triggers a fee
    Given I have a "goods" "standard_international" licence
    And i add an operating centre and increase the vehicle total authority
    When i increase my lgv authorisation and delete the new operating centre
    And complete the financial evidence page
    Then the review and declaration page should display pay and submit
    And the variation fee should be triggered

  Scenario: Variation created by a caseworker with "Fee Required" triggers a fee
    Given I have a "goods" "standard_international" licence
    When i create a lgv authorisation increase variation with "Fee Required" on internal
    Then the variation fee is required on internal

  Scenario: Variation created by a caseworker with "No Fee Required" doesn't trigger a fee
    Given I have a "goods" "standard_international" licence
    When i create a lgv authorisation increase variation with "No Fee Required" on internal
    Then the variation fee is not required on internal

  Scenario: Accessibility scan the pages (use new Accessibility library in pom)
    Given I have a "goods" "standard_international" licence
    When i scan the various operating centre and authorisation pages
    Then no issues should be present on the page
