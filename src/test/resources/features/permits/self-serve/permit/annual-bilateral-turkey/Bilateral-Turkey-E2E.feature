@bilateral_turkey
#@eupa_regression
Feature: Bilaterals Turkey end to end happy path journey including validations

  Background:
    Given I have a "goods" "standard_international" licence
    And I have selected Turkey and I am on the Bilateral application overview page

  @olcs-27606 @OLCS-28230
  Scenario: Verify that Bilaterals Turkey (after selecting YES on third country) page end to end happy path journey is correct and application is submitted successfully
    When I click on Turkey country link on the Application overview page
    Then I am navigated to Turkey essential information page with correct information and content
    When I select continue button on the Bilateral Turkey essential information page
    Then I am on the Bilateral Turkey Period Selection page with correct information and content
    When I select continue button on the Bilateral period selection page
    Then I am on the Bilateral Permit usage page with correct information and content
    When I select continue button on the Bilateral permit usage page
    Then I am on the Turkey third country page with correct information and content
    When I save and continue on the Turkey third country page
    Then I should get the validation error message to select one option
    When I select Yes radio button on the Turkey third country page
    And  I save and continue on the Turkey third country page
    Then I am on the annual bilateral number of permits page with correct information and content
    And I click save and continue
    Then I should get the validation error message on the number of permits page
    When I enter the number of bilateral permits required
    Then I am on the Annual Bilateral Turkey check your answers page with correct information and content
    When I click Confirm and return to overview
    Then the status of Answer questions for individual countries section for the selected country is set as complete
    And  the status of answers questions for individual countries as complete
    When I click on read declaration on the application overview page
    Then I am taken to the bilateral declaration Page with correct information and content
    When I click on Accept and continue on the Declaration page without selecting declaration checkbox
    Then I should get the correct error message on the declaration page
    When I click on Accept and continue on the Declaration page
    Then I am on the permit fee page for annual bilateral application with correct information and content
    When I submit and pay the Bilateral fee
    Then I am on the Annual Bilateral application submitted page with correct information and content
    When I click 'go to permits' dashboard on the submitted page
    Then the application goes to valid status
    When I click on my licence on the permits dashboard page
    Then The content and information on valid permits is correct
    When I select return to permits dashboard hyperlink
    Then I am navigated back to permits dashboard page
    #The above scenario need to fix as part of API call

  @olcs-27606 @OLCS-28230 @eupa_regression
  Scenario: Verify that Bilaterals Turkey (after selecting No on third country) page end to end happy path journey is correct and application is submitted successfully
    When I click on Turkey country link on the Application overview page
    Then I am navigated to Turkey essential information page with correct information and content
    When I select continue button on the Bilateral Turkey essential information page
    Then I am on the Bilateral Turkey Period Selection page with correct information and content
    When I select continue button on the Bilateral period selection page
    Then I am on the Bilateral Permit usage page with correct information and content
    When I select continue button on the Bilateral permit usage page
    Then I am on the Turkey third country page with correct information and content
    When I save and continue on the Turkey third country page
    Then I should get the validation error message to select one option
    When I select No radio button on the Turkey third country page
    Then I get advisory text that I don't need Turkey permits
    When I save and continue on the Turkey third country page
    Then I am navigated to the cancel application page