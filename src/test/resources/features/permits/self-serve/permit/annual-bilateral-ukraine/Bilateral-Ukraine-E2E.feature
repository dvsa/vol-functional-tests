@bilateral_ukraine
Feature: Bilaterals Ukraine end to end happy path journey including validations

  Background:
    Given I have a "goods" "standard_international" licence
    And  I am on the VOL self-serve site
    And I have selected Ukraine and I am on the Bilateral application overview page

   @olcs-27607 @OLCS-28230
  Scenario: Verify that Bilaterals Ukraine (Euro3 or Euro4 emission) page end to end happy path journey is correct and application is submitted successfully
     When I click on Ukraine country link on the Application overview page
     Then I am navigated to Ukraine essential information page with correct information and content
     When I select continue button on the Bilateral Ukraine essential information page
     Then I am on the Bilateral Ukraine Period Selection page with correct information and content
     When I select continue button on the Bilateral period selection page
     Then I am on the Bilateral Permit usage page with correct information and content
     When I select continue button on the Bilateral permit usage page
     Then I am on the Ukraine emissions standards page with correct information and content
     When I save and continue on the Ukraine emissions standards page
     Then I should get the validation error message to select one option
     When I select Euro 3 or Euro 4 radio button on the Ukraine emissions standards page
     And  I save and continue on the Ukraine emissions standards page
     Then I am on the annual bilateral number of permits page with correct information and content
     And I save and continue
     Then I should get the validation error message on the number of permits page
     When I enter the number of bilateral permits required
     Then I am on the Annual Bilateral Ukraine check your answers page with correct information and content
     When I click Confirm and return to overview
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

  @olcs-27607 @OLCS-28230
  Scenario: Verify that Bilaterals Ukraine (Euro5 or Euro6 emission) page end to end happy path journey is correct and application is submitted successfully
    When I click on Ukraine country link on the Application overview page
    Then I am navigated to Ukraine essential information page with correct information and content
    When I select continue button on the Bilateral Ukraine essential information page
    Then I am on the Bilateral Ukraine Period Selection page with correct information and content
    When I select continue button on the Bilateral period selection page
    Then I am on the Bilateral Permit usage page with correct information and content
    When I select continue button on the Bilateral permit usage page
    Then I am on the Ukraine emissions standards page with correct information and content
    When I save and continue on the Ukraine emissions standards page
    Then I should get the validation error message to select one option
    When I select Euro 5, Euro 6 or higher emission standard radio button on the Ukraine emissions standards page
    Then I get advisory text that I don't need Ukraine permits
    When I save and continue on the Ukraine emissions standards page
    Then I am navigated to the cancel application page


