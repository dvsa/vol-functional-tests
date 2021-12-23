@lgv
Feature: Publications are generated correctly upon creating an lgv increase variation

  Scenario: A submitted LGV Only variation creates a publication correctly on internal
    Given I have a "GB" lgv only application
    And i create and submit an lgv only operating centre variation
    And i navigate to the application publications page
    Then the new application publication for LGV Only should be correct on internal

  Scenario: A submitted LGV Only variation displays the correct information publication overview on self serve search
    Given I have a valid "GB" lgv only licence
    And i create and submit an lgv only operating centre variation
    When the corresponding publication is generated and published
    And enter

  Scenario: A submitted LGV mixed variation displays the correct information publication overview on self serve search
    Given I have a submitted "goods" "standard_international" application
    And i create and submit an lgv only operating centre variation
    When the corresponding publication is generated and published
    Then enter

  Scenario: A publication can be created on an LGV only auth increase variation initiated by the caseworker
