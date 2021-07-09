@Deprecated
Feature: Short term ECMT APSG with sectors number of permits page

  Background:
    Given I have a "goods" "standard_international" licence
    And I am on the VOL self-serve site
    And I am on the short term number of permits page

  @OLCS-25088 @OLCS-25087 @OLCS-27781
  Scenario: Page heading and advisory messages are displayed correctly
    When I click the back link
    Then the user is navigated to the overview page with the number of permits page status as not started yet
    When I select number of permits hyperlink from overview page
    Then I am taken back to short term number of permits page
    Then the page heading on the short term number of permits page is displayed correctly
    And I save and continue
    And I should get the number of permits page error message on short term
    And I save and return to overview
    And I should get the number of permits page error message on short term
    And I enter the number of permits required more than the authorised vehicles
    And I save and continue
    Then I should get the validation error message
    And I enter the valid number of short term permits required
    And I save and continue
    Then the user is navigated to the next page

  @OLCS-25088
  Scenario: Save and continue to overview updates the status as completed
    When I enter the valid number of short term permits required
    And I select save and return overview link
    Then the user is navigated to the overview page with the number of permits page status as completed