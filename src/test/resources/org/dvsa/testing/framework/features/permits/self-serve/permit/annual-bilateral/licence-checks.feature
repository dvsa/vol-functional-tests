@Deprecated
Feature: Annual bilateral licence page checks

  Background:
    Given I have a "goods" "standard_international" licence
    And I am on the VOL self-serve site

  @EXTERNAL @OLCS-23117 @bilateral_cabotage_only @OLCS-27444 @OLCS-27781
  Scenario: Able to apply for a permit when there are no existing application on the selected licence
    When I have began applying for an Annual Bilateral Permit
    Then I am able to complete an annual bilateral permit application

  @EXTERNAL @OLCS-23117 @bilateral_cabotage_only @OLCS-27444
  Scenario: Not able to apply for a permit when selected licence has existing application
    Given I have partial annual bilateral applications
    When I try applying with a licence that has an existing application
    Then I should be informed that there is already an active permit application for this licence

  @EXTERNAL @OLCS-23117 @bilateral_cabotage_only @OLCS-26045
  Scenario: Shows details of active application already on licence
    Given I have partial annual bilateral applications
    When I try applying with a licence that has an existing application
    And I save and continue
    Then I should be on the bilateral overview page for the active application already on the licence