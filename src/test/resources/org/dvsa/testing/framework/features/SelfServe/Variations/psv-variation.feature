Feature: PSV variation to make operating centre and authorisation changes

  Scenario: Attempt to increase authorisation above 2 on a PSV Restricted licence
    Given i have a valid "public" "restricted" licence
    And i begin an operating centre and authorisation variation
    And i create a new operating centre with "3" vehicles and "0" trailers
    Then the operating centre cannot be added