@lgv
Feature: Publications display the right LGV related information on LGV only licences

  @int_regression
  Scenario: A submitted LGV Only application generated a publication correctly on internal
    Given I have a submitted "GB" lgv only application
    And i navigate to the application publications page
    Then the new application publication for LGV Only should be correct on internal

  @ss_regression
  Scenario: A submitted LGV Only application displays the correct information upon publishing
    Given I have a submitted "GB" lgv only application
    When the corresponding publication is generated and published
    Then the publication is visible via self serve search
    And the new application publication text for LGV Only should be correct on self serve

  Scenario: A submitted Goods SN application displays the correct information upon publishing
    Given I have a submitted "goods" "standard_national" application
    When the corresponding publication is generated and published
    Then the publication is visible via self serve search
    And the new application publication text for Non SI Goods should be correct on self serve

  Scenario: A granted LGV Only application displays the correct information upon publishing
    Given I have a submitted "GB" lgv only application
    And the licence is granted
    When the corresponding publication is generated and published
    Then the publication is visible via self serve search
    And the application granted publication text for LGV Only should be correct on self serve

  Scenario: A refused LGV Only application displays the correct information upon publishing
    Given I have a submitted "GB" lgv only application
    And the application has been refused
    When the corresponding publication is generated and published
    Then the publication is visible via self serve search
    And the application refused publication text for LGV Only should be correct on self serve

  Scenario: A withdrawn LGV Only application displays the correct information upon publishing
    Given I have a submitted "GB" lgv only application
    And the application has been withdrawn
    When the corresponding publication is generated and published
    Then the publication is visible via self serve search
    And the application withdrawn publication text for LGV Only should be correct on self serve

  @smoketest
  Scenario: A LGV Only licence displays the correct licence information upon publishing (application shows nothing)
    Given I have a submitted "GB" lgv only application
    And the licence is granted
    When the corresponding publication is generated and published
    Then the publication is visible via self serve search
    And the licence view of the publication for LGV Only is correct on self serve

  Scenario: A PSV SI licence displays the correct licence information upon publishing
    Given i have a valid "public" "standard_international" licence
    When the corresponding publication is generated and published
    Then the publication is visible via self serve search
    And the licence view of the publication for PSV SI is correct on self serve

  Scenario: A Goods SN licence displays the correct licence information upon publishing
    Given i have a valid "goods" "standard_national" licence
    When the corresponding publication is generated and published
    Then the publication is visible via self serve search
    And the licence view of the publication for Goods SN is correct on self serve