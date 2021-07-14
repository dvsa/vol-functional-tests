Feature: Internal permits Withdraw Application

  Background:
    Given I have a "goods" "standard_international" licence
    And I am on the VOL internal site
    And i create an admin and url search for my licence
    And I apply for an ECMT APGG Euro5 or Euro 6 application


  @INTERNAL @OLCS-21284 @olcs-27682 @internal_annual_ecmt_apgg_euro5_or_euro6 @eupa_regression
  Scenario: I should see withdraw button under the Application details when application is in status of Under Consideration
    When I click on submit button
    And I pay fee for application
    Then I am in application details page, I should see withdraw button

  @INTERNAL @OLCS-21284 @olcs-27682 @internal_annual_ecmt_apgg_euro5_or_euro6 @eupa_regression
  Scenario: Verify that an Application status goes to Withdrawn successfully when Application is withdrawn from Internal
    And I click on submit button
    And I pay fee for application
    And I am in application details page, I should see withdraw button
    Then In application details page, I withdraw Permit Application
    And I am in application details page, I should not see withdraw button
    And I am in application details page, I should see application as withdrawn

  @INTERNAL @OLCS-21284 @Deprecated
  Scenario: Withdraw application from details page
    When I click on submit button
    And I pay fee for application
    Then In application details page, I withdraw Permit Application
    And I am in application details page, I should not see withdraw button
    And I am in application details page, I should see application as withdrawn