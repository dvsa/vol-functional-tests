Feature: Internal IRHP permits fee validation

  Background:
    Given I have valid Goods standard_international VOL licence
    And I am on the VOL internal site
    And I am viewing a good operating licence on internal
    And I am viewing a licences IRHP section
    And  I apply for an ECMT APGG Euro5 or Euro 6 application

  @INTERNAL @OLCS-20954 @WIP @internal_annual_ecmt_apgg_euro5_or_euro6 @olcs-27682
  Scenario: View permit fee calculated correctly in fee tab
    And I'm  viewing my saved application in internal
    When I am on the fee tab page
    Then I should see Fee Amount calculated correctly
   And I should see Outstanding balance calculated correctly

  @INTERNAL @OLCS-20954 @WIP @Deprecated
  Scenario: View permit fee calculated correctly in First Fee Tab
    When I am on the first fee tab page
    Then I should see Fee Amount calculated correctly
    And I should see Outstanding balance calculated correctly

  @INTERNAL @OLCS-20954 @WIP @olcs-27682 @internal_annual_ecmt_apgg_euro5_or_euro6
  Scenario: View permit fee calculated correctly in Permit details Page
    And  I'm  viewing my saved application in internal
    When I am on the fee tab page
    And I click the application link on the fees page
    Then In Details page, I should see Fee Amount calculated correctly
    And In Details page, I should see Outstanding balance calculated correctly


  @INTERNAL @OLCS-20954 @WIP @olcs-27682 @internal_annual_ecmt_apgg_euro5_or_euro6
  Scenario: View permit fee calculated correctly when paying fee
    And I'm  viewing my saved application in internal
    When I am on the fee tab page
    And I select application to pay
    Then I should see Pay Fee Amount calculated correctly