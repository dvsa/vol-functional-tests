@psv-restricted-variation @psv-vehicle-size

  Feature: PSV variation applications for Restricted licences

    Background:
      Given I have a "public" "restricted" licence with "1" vehicle authorisation
      And i begin an operating centre and authorisation variation
      And i create a new operating centre with "2" vehicles and "0" trailers
      And i increase total PSV authorisation to "2" vehicles

    Scenario: Restricted variation for Both - No to small vehicles
      When i select the Vehicle size section
      And i select Vehicle Size "both"
      And i answer "No" to the Operating small vehicles question
      And i complete the Small vehicles conditions page
      And i complete the Documentary evidence - small vehicles page
      And i complete the Documentary evidence - main occupation page
      And i complete the Main occupation undertakings page
      And i complete the Limousines page selecting "Yes"
      Then the completed Restricted Both No sections should be marked "Updated"

