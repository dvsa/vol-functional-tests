Feature: Internal Feature toggle

  Background:
    Given I am on the VOL internal site

  @INTERNAL @OLCS-20855 @TOGGLE-ECMT
  Scenario: Admin internal caseworker is able to see option to toggle permits
    When I log in as an internal user with admin privileges
    And feature toggle for permits has been enabled
    Then I should be able to see the feature toggle option

  @INTERNAL @OLCS-20855 @TOGGLE-ECMT
  Scenario: Normal user should not see the option to toggle permits
    When I log in as an internal user with normal privileges
    Then I should NOT be able to see the feature toggle option

  @INTERNAL @OLCS-20855 @TOGGLE-ECMT
  Scenario: Disabling ECMT internal disables ECMT features on internal
    Given I have a "goods" "standard_international" licence
    And I log in as an internal user with admin privileges
    And disable all internal ECMT feature toggles
    When i create an admin and url search for my licence
    Then internal users should not be able to create ECMT Permit applications

  @INTERNAL @OLCS-20855 @TOGGLE-ECMT
  Scenario: Disabling ECMT feature on external prevents external users from applying for ECMT Permits
    Given I have a "goods" "standard_international" licence
    And I log in as an internal user with admin privileges
    And disable all external ECMT feature toggles
    When I sign on as an external user
    Then the permits tab should not be displayed