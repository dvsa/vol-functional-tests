@psv-standard-variation

  Feature: PSV variation applications for Standard licences

    Background:
      Given i have a valid "public" "standard_national" licence
      And i begin an operating centre and authorisation variation
      And i create a new operating centre with "2" vehicles and "0" trailers
      And i increase total PSV authorisation to "7" vehicles

    Scenario: Standard National variation for Small vehicles less than 9 seats
      When i select the Vehicle size section
      And i select Small vehicles - less than 9 seats
      And i complete the Small vehicles conditions page
      And i complete the Documentary evidence - small vehicles page
      And i complete the Limousines and novelty vehicles on the small vehicles journey
      Then the completed Small vehicle sections should be marked Updated
