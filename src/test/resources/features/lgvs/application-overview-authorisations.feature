@lgv
Feature: Authorisation updates display correctly on Internal for caseworkers

  @lgv-smoke
  Scenario: LGV Only application authorisation updates correctly
    Given I have a submitted "GB" lgv only application
    When i am on the internal application overview page
    Then the LGV Only authorisation on the application overview screen should display "0" lgvs to "5" lgvs
    And the application overview displays "0" operating centres to "0" operating centres

  @lgv-smoke
  Scenario: LGV Mixed application authorisation updates correctly
    Given I have a "goods" "standard_international" application
    When i am on the internal application overview page
    Then the LGV Mixed authorisation on the application overview screen should display "0" hgvs to "5" hgvs and "0" lgvs to "0" lgvs
    And the application overview displays "0" operating centres to "1" operating centres

  Scenario: Goods SN application authorisation updates correctly
    Given I have a submitted "goods" "standard_national" application
    When i am on the internal application overview page
    Then the Goods Standard National authorisation on the application overview screen should display "0" vehicles to "5" vehicles and "0" trailers to "5" trailers
    And the application overview displays "0" operating centres to "1" operating centres

  Scenario: PSV SI application authorisation updates correctly
    Given I have a submitted "public" "standard_international" application
    When i am on the internal application overview page
    Then the PSV Standard International authorisation on the application overview screen should display "0" vehicles to "5" vehicles
    And the application overview displays "0" operating centres to "1" operating centres

  Scenario: LGV Only variation authorisation updates correctly
    Given I have a valid "GB" lgv only licence
    And i create and submit an lgv authorisation variation with 5 more LGVs
    When i am on the internal variation overview page
    Then the LGV Only authorisation on the application overview screen should display "5" lgvs to "10" lgvs

  Scenario: LGV Mixed variation authorisation updates correctly
    Given i have a valid "goods" "standard_international" licence
    When i create an operating centre variation with "7" hgvs and "6" lgvs
    When i am on the internal variation overview page
    Then the LGV Mixed authorisation on the application overview screen should display "5" hgvs to "7" hgvs and "0" lgvs to "6" lgvs
    And the application overview displays "1" operating centres to "1" operating centres

  Scenario: Goods SN variation authorisation updates correctly
    Given i have a valid "goods" "standard_national" licence
    And i create and submit an operating centre variation with "8" hgvs and "0" lgvs
    When i am on the internal variation overview page
    Then the Goods Standard National authorisation on the application overview screen should display "5" vehicles to "8" vehicles and "5" trailers to "5" trailers
    And the application overview displays "1" operating centres to "1" operating centres

  Scenario: PSV SI variation authorisation updates correctly
    Given i have a valid "public" "standard_international" licence
    And i create and submit an operating centre variation with "8" hgvs and "0" lgvs
    When i am on the internal variation overview page
    Then the PSV Standard International authorisation on the application overview screen should display "5" vehicles to "8" vehicles
    And the application overview displays "1" operating centres to "1" operating centres