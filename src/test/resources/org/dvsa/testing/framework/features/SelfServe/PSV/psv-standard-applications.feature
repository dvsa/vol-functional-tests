@psv-standard-application @psv-vehicle-size @ss_regression

  Feature: PSV new applications for Standard licences

    Background:
      Given i have a self serve account
      And i create a new "public" "standard_international" application
      And i select operating centres and add a PSV operating centre for "3" vehicles

    Scenario: Standard International new application for Small vehicles less than 9 seats
      When i select the Vehicle size section
      And i select Vehicle Size "small"
      And i complete the Small vehicles conditions page
      And i complete the Documentary evidence - small vehicles page
      And i complete the Limousines page selecting "No"
      Then the completed Small vehicle sections should be marked "Complete"
