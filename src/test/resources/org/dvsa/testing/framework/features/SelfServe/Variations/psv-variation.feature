Feature: PSV variation to make operating centre and authorisation changes

  Scenario: Attempt to increase operating centre authorisation above 2 on a PSV Restricted licence
    Given i have a valid "public" "restricted" licence
    And i begin an operating centre and authorisation variation
    And i create a new operating centre with "3" vehicles and "0" trailers
    Then the operating centre cannot be added

  Scenario: Attempt to increase total authorisation above 2 on a PSV Restricted licence
    Given i have a valid "public" "restricted" licence
    And i begin an operating centre and authorisation variation
    And i create a new operating centre with "2" vehicles and "0" trailers
    And i increase total PSV authorisation to "3" vehicles
    Then the increase in PSV authorisation is not allowed

  Scenario: Submit PSV Restricted variation to increase authorisation to 2 vehicles
    Given I have a "public" "restricted" licence with "1" vehicle authorisation
    And i begin an operating centre and authorisation variation
    And i create a new operating centre with "2" vehicles and "0" trailers
    And i increase total PSV authorisation to "2" vehicles
    And The variation is submitted