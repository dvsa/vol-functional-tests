Feature:  HTML snapshot

  Background:
    Given I have valid Goods standard_international VOL licence

  @INTERNAL @OLCS-20958 @ECMT @annual_ecmt_apgg_euro5_or_euro6
  Scenario: Doc is generated from ECMT permit application submitted externally
    And I am on the VOL self-serve site
    And I have completed an ECMT application
    And I am viewing a good operating licence on internal
    When I view the annual ECMT Permits documentation
    Then the annual ECMT Permits HTML document should have the correct information

  @INTERNAL @OLCS-20958 @ECMT @WIP
  Scenario: Doc is generated when ECMT permit application is submitted internally
    And I am viewing a good operating licence on internal
    And I apply for an ECMT APGG Euro5 or Euro 6 application
    And submit my ECMT permit application
    When I view the annual ECMT Permits documentation on internal
    Then the annual ECMT Permits HTML document should have the correct information

  @EXTERNAL @MULTILATERAL @WIP
  Scenario: Snapshot is generated when user submits permit on external
    And I am on the VOL self-serve site
    When I have a valid annual multilateral permit
    And I am on the VOL internal site
    And A case worker is reviewing my docs & attachments
    Then An HTML snapshot for my annual multilateral permit is generated

  @INTERNAL @OLCS-23010 @MULTILATERAL @WIP
  Scenario: Has expected text
    And A case worker is reviewing my annual multilateral snapshot
    Then all text for annual multilateral snapshot is as expected

  @SNAPSHOT @OLCS-27366 @bilateral_cabotage_only
  Scenario: Snapshot generated when bilateral application is submitted in selfserve
    And I am on the VOL self-serve site
    When I have a valid annual bilateral permit
    And I am on the VOL internal site
    And A case worker is reviewing my docs & attachments
    Then an HTML snapshot for annual bilateral permit is generated
    Then text for annual bilateral snapshot is displayed as expected