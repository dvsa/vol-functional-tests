@psv-standard-variation @psv-vehicle-size @ss_regression

  Feature: PSV variation applications for Standard licences

    Background:
      Given i have a valid "public" "standard_national" licence
      And i begin an operating centre and authorisation variation
      Then i create a new operating centre with "2" vehicles and "0" trailers
      And i increase total PSV authorisation to "7" vehicles

    Scenario: Standard National variation for Small vehicles less than 9 seats
      When i select the Vehicle size section
      And i select Vehicle Size "small"
      Then i complete the Small vehicles conditions page
      And i complete the Documentary evidence - small vehicles page
      When i complete the Limousines and novelty vehicles on the small vehicles journey
      Then the completed Small vehicle sections should be marked "Updated"

    Scenario: Standard National variation for vehicles 9 seats and above
      When i select the Vehicle size section
      And i select Vehicle Size "nine_and_above"
      Then i complete the Vehicles with nine seats or more page
      And i complete the Limousines page selecting "Yes"
      Then the completed 9 vehicles and above sections should be marked "Updated"
