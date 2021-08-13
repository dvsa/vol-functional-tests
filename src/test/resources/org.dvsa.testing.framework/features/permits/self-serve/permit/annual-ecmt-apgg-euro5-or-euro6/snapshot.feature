Feature:  HTML snapshot

  Background:
    Given I have a "goods" "standard_international" licence
    And I am on the VOL self-serve site

  @INTERNAL @OLCS-20958 @ECMT @annual_ecmt_apgg_euro5_or_euro6 @eupa_regression
  Scenario: Doc is generated from ECMT permit application submitted externally
    And I have completed an ECMT application
    And i create an admin and url search for my licence
    When I view the annual ECMT Permits documentation
    Then the annual ECMT Permits HTML document should have the correct information

  @SNAPSHOT @OLCS-27366 @bilateral_cabotage_only @WIP
  Scenario: Snapshot generated when bilateral application is submitted in selfserve
#    TODO below step
    When I have a valid annual bilateral permit
    And I am on the VOL internal site
    And A case worker is reviewing my docs & attachments
    Then an HTML snapshot for annual bilateral permit is generated
    Then text for annual bilateral snapshot is displayed as expected