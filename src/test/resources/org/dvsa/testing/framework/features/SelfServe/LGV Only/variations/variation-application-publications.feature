@lgv
@lgv-smoke
Feature: Publications are generated correctly upon creating an lgv increase variation

  Scenario: A submitted LGV Only variation creates a publication correctly on internal
    Given I have a valid "GB" lgv only licence
    And i create and submit an lgv authorisation variation with 5 more LGVs
    When i navigate to the application publications page
    Then the variation publication for LGV Only should be correct on internal with 5 more lgvs

  Scenario: A submitted LGV Only variation displays the correct information publication overview on self serve search
    Given I have a valid "GB" lgv only licence
    And i create and submit an lgv authorisation variation with 5 more LGVs
    When the corresponding publication is generated and published
    Then the publication is visible via self serve search
    And the variation publication for LGV Only should be correct on self serve with 5 more lgvs

  Scenario: A publication can be created on an LGV only auth increase variation initiated by the caseworker
    Given I have a valid "GB" lgv only licence
    And I create and save an lgv authorisation variation on internal with 5 more LGVs
    And I publish the application on internal
    When I view the application publications page
    Then the variation publication for LGV Only should be correct on internal with 5 more lgvs