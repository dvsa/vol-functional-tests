Feature: Internal Case Worker submits an ECMT Permit application

  Background:
    Given I have valid Goods standard_international VOL licence
    And I am on the VOL internal site
    And I am viewing a good operating licence on internal
    And I am viewing a licences IRHP section
    And I apply for an ECMT APGG Euro5 or Euro 6 application

  @INTERNAL @OLCS-20955 @OLCS-25085 @olcs-27682 @internal_annual_ecmt_apgg_euro5_or_euro6
  Scenario: To verify that Application status goes to UC and snapshot is generated when case worker successfully submits an Application
    Then In application details page, I should see Submit button
    When I click on submit button
    And I pay fee for application
    Then I am in application details page, I should see application is in UC status
    And I am in Docs and attachments page, I should see the snapshot generate successfully