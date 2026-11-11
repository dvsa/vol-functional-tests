@safety-compliance @ss_regression
Feature: Safety and compliance details on a licence

  @safety-inspector-add
  Scenario: Add an internal safety inspector
    Given i have a valid "goods" "standard_national" licence
    When I add an internal safety inspector
    Then the safety inspector is listed with external status "No"

  @safety-inspector-edit-remove
  Scenario: Edit and remove a safety inspector
    Given i have a valid "goods" "standard_national" licence
    When I add an internal safety inspector
    And I edit the safety inspector
    Then the edited safety inspector is listed
    When I remove the safety inspector
    Then the safety inspector is no longer listed

  @external-safety-provider
  Scenario: Add an external safety inspection provider
    Given i have a valid "goods" "standard_national" licence
    When I add an external safety inspection provider
    Then the safety inspector is listed with external status "Yes"

  @safety-validation
  Scenario: Reject invalid safety inspection settings and save valid settings
    Given i have a valid "goods" "standard_national" licence
    When I submit invalid safety inspection settings
    Then the invalid safety inspection settings are rejected
    When I correct and save the safety inspection settings
    Then the valid safety inspection settings are saved

  @psv-safety-variant
  Scenario: PSV safety section does not include trailer inspection fields
    Given i have a valid "public" "standard_international" licence
    When I view the safety and compliance section
    Then the PSV safety section does not include trailer inspection fields
