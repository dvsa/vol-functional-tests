@Deprecated
Feature: Annual bilateral Permits dashboard page

  Background:
    Given I have a "goods" "standard_international" licence
    And I am on the VOL self-serve site

  @OLCS-23131 @EXTERNAL @OLCS-21112 @BilateralNorway
  Scenario: Ongoing annual bilateral displayed in reference descending order
    And I have partial annual bilateral applications
    Then ongoing permits should be sorted by reference number in descending order

  @OLCS-23227 @CLOSES-WINDOW @EXTERNAL @OLCS-21112 @bilateral_cabotage_only
  Scenario: Annual bilateral permit applications are shown above any ECMT annual permit applications Given I am in the permit dashboard
    And have valid permits
    And I have a valid annual bilateral noway cabotage only permit