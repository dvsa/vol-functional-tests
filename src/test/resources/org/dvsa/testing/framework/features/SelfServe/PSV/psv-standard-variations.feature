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

    Scenario: Standard National variation for vehicles 9 seats and above
      When i select the Vehicle size section
      And i select Vehicles 9 seats and above
      And i complete the Vehicles with nine seats or more page
      And i complete the Limousines page selecting Yes
      Then the completed 9 vehicles and above sections should be marked Updated
