@psv-restricted-application

  Feature: PSV new applications for Restricted licences

    Background:
      Given i have a self serve account
      And i create a new "public" "restricted" application
      And i select operating centres and add a PSV operating centre for "2" vehicles

    Scenario: Restricted new application for Both - Yes to Small vehicles
      When i select the Vehicle size section
      And i select Vehicle Size "both"
      And i answer "Yes" to the Operating small vehicles question
      And i complete the Small vehicles conditions page after answering Yes
      And i complete Written explanation small vehicles
      And i complete the Documentary evidence - main occupation page
      And i complete the Main occupation undertakings page
      And i complete the Limousines page selecting "No"
      Then the completed Both Yes sections should be marked "Complete"


