@Deprecated
Feature: Annual bilateral Permits dashboard page

  Background:
    Given I have a "goods" "standard_international" licence
    And I am on the VOL self-serve site
    And I have partial annual bilateral applications

  @EXTERNAL  @OLCS-27073 @bilateral_cabotage_only
  Scenario: Bilateral Permits dashboard displays the Ongoing applications content and values correctly
    When I navigate to the Permits dashboard page from the Bilaterals Overview page
    Then my Bilaterals permit should be under the ongoing permit application table with correct columns and values

  @EXTERNAL @OLCS-27073 @bilateral_cabotage_only
  Scenario: Bilateral Permits dashboard displays the Issued permits and certificates content and values correctly
    When I Submit my Annual bilateral partial application and navigate to the Permits dashboard
    Then my Bilaterals permit should be under the Issued permit applications table with correct columns and values

  @OLCS-23131 @EXTERNAL @OLCS-21112 @BilateralNorway
  Scenario: Ongoing annual bilateral displayed in reference descending order
    And I have partial annual bilateral applications
    Then ongoing permits should be sorted by reference number in descending order

  @OLCS-23227 @CLOSES-WINDOW @EXTERNAL @OLCS-21112 @bilateral_cabotage_only
  Scenario: Annual bilateral permit applications are shown above any ECMT annual permit applications Given I am in the permit dashboard
    And have valid permits
    And I have a valid annual bilateral noway cabotage only permit
    Then I can see the Annual Bilateral Permit applications above ECMT Annual Permit applications