@lgv
Feature: Interim requests are available upon the correct circumstances

  Scenario: Can request interim on LGV total authorisation increase
    Given I create a new external user
    And i have a "goods" "standard_international" licence with a hgv authorisation of "5" lgv authorisation of "5" in traffic area "1"
    When i create an operating centre variation with "5" hgvs and "7" lgvs
    Then complete the financial evidence page
    And i can request an interim on the "variation"

  Scenario: Cannot request interim on LGV total authorisation decrease
    Given I create a new external user
    And i have a "goods" "standard_international" licence with a hgv authorisation of "5" lgv authorisation of "5" in traffic area "1"
    When i create an operating centre variation with "5" hgvs and "4" lgvs
    And i cannot request an interim on the "variation"

  Scenario Outline: Can request interim on HGV total and operating centre authorisation increase (lgv mixed and normal)
    Given I create a new external user
    And i have a "<operatorType>" "<licenceType>" licence with a hgv authorisation of "5" lgv authorisation of "5" in traffic area "1"
    When i create an operating centre variation with "8" hgvs and "5" lgvs
    Then complete the financial evidence page
    And i can request an interim on the "variation"

    Examples:
    | operatorType | licenceType            |
    | goods        | standard_international |
    | goods        | standard_national      |

  Scenario Outline: Cannot request interim on HGV total and operating centre authorisation decrease
    Given I create a new external user
    And i have a "<operatorType>" "<licenceType>" licence with a hgv authorisation of "5" lgv authorisation of "5" in traffic area "1"
    When i create an operating centre variation with "3" hgvs and "5" lgvs
    Then i remove the 2 extra vehicles
    And i cannot request an interim on the "variation"

    Examples:
      | operatorType | licenceType            |
      | goods        | standard_international |
      | goods        | standard_national      |

  Scenario: Can request interim on trailers total authorisation increase
    Given I create a new external user
    And i have a "goods" "standard_international" licence with a hgv authorisation of "5" lgv authorisation of "5" in traffic area "1"
    When i create an operating centre variation with 7 trailers
    And i can request an interim on the "variation"

  Scenario: Cannot request interim on trailers total authorisation decrease
    Given I create a new external user
    And i have a "goods" "standard_international" licence with a hgv authorisation of "5" lgv authorisation of "5" in traffic area "1"
    When i create an operating centre variation with 3 trailers
    And i cannot request an interim on the "variation"

  Scenario: Can request interim on applying for a new licence
    Given I have a "goods" "standard_international" application
    When i have logged in to self serve
    Then i can request an interim on the "application"
