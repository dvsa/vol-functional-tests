Feature:  HTML snapshot

  Background:
    Given I have a "goods" "standard_international" licence

  @INTERNAL @OLCS-20958 @ECMT @annual_ecmt_apgg_euro5_or_euro6 @eupa_regression
  Scenario: Doc is generated from ECMT permit application submitted externally
    And I have completed an ECMT application
    And i create an admin and url search for my licence
    When I view the annual ECMT permits documentation
    Then the annual ECMT Permits HTML document should have the correct information

  @SNAPSHOT @OLCS-27366 @bilateral_cabotage_only
  Scenario: Snapshot generated when bilateral application is submitted in selfserve
    And  I have selected Morocco and I am on the Bilateral application overview page
    And I submit the application for empty entry single journey on selection of Morocco link on overview page
    When I accept declaration and submit the application
    And i create an admin and url search for my licence
    And I view the annual bilateral permits documentation
    Then text for annual bilateral snapshot is displayed as expected