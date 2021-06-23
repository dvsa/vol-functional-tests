@EXTERNAL @shortterm_apgg_euro5_or_euro6
Feature: Short term ECMT APGG Euro 5 or Euro 6 end to end happy path journey including issued permits

  Background:
    Given I have a "goods" "standard_international" licence
    And  I am on the VOL self-serve site
    And I select Short term ecmt permit on the select permit page
    And I select year on the select year page
    And I select short term ecmt period
    And I select any licence number for short term permit

   @OLCS-25668 @shortterm_apgg_euro5_or_euro6e2e @OLCS-28226 @OLCS-28276
  Scenario: Verify that ECMT short term application flow is correct and application is submitted successfully with status of  Under Consideration
    Then I am on short term ECMT Application overview Page
    When I click the back link
    Then I am navigated back to the permits dashboard page with my application status shown as Not yet Submitted
    Then I complete the Check if you need ECMT permits section and click save and continue
    Then I complete APGG Cabotage page section and click save and continue
    Then I complete Certificates required page section and click save and continue
    Then I complete Countries with limited permits section and click save and continue
    Then I complete Number of permits required section and click save and continue
    Then I am on the Permits start date page
    Then I complete Euro emissions standard page section and click save and continue
    Then I click confirm and continue on the Check your answers page
    Then I click on Accept and continue on the Declaration page
    Then I click on Submit and Pay button on the Permit fee page and complete the payment
    When I click on the Finish button on the Application submitted page
    Then I am navigated back to the permits dashboard page with my application status shown as Under Consideration
    When I'm  viewing my saved Short term ECMT APGG application in internal and Granting Permit
    When I login back to the External to view the application in status of awaiting fee
    Then the application status on the external changes to awaiting fee
    When I accept and pay the APGG issuing fee
    And I make card payment
    Then My application status changes to Valid