@EXTERNAL @annual_ecmt_apgg_euro5_or_euro6
Feature: Number of permits required page

  Background:
    Given I have valid Goods standard_international VOL licences
    And I am on the VOL self-serve site
    And I am on the number of permits page

  @EXTERNAL @OLCS-24823 @ECMT @Test3 @OLCS-27781 @OLCS-28275
  Scenario: Page Heading and Advisory texts and validation messages are displayed
    And the page heading on the ECMT number of permits page is displayed correctly
    And the advisory texts are displayed correctly
    And I have not specified an amount for permits
    And I save and continue
    And I should get the ECMT number of permits page error message
    And I select save and return overview link
    And I should get the ECMT number of permits page error message
    And I specify the number of permits
    And I select save and return overview link
    Then I should be on the Annual ECMT overview page

  @EXTERNAL @OLCS-21286 @OLCS-24823 @ECMT @DEV @Test3 @OLCS-28275
  Scenario: Specifies a number larger than their number of authorised vehicles
    Given I have specified a number greater than the number of authorised vehicles
    When I save and continue
    Then I should get an error message

  @EXTERNAL @OLCS-21286 @ECMT @DEV @Test3 @olcs-27581 @OLCS-28275
  Scenario: Application back button
    Given I have not specified the amount of permits I'd like
    When I go back
    Then I should be on the Annual ECMT overview page

  @EXTERNAL @OLCS-17815 @ECMT @Test3 @olcs-27581 @OLCS-27781 @OLCS-28275
  Scenario: Pay Fees from Fee Tab on Self serve screen
    And I specify the number of permits
    When  I select save and return overview link
    Then I am on the annual ECMT overview page
    When I go back
    Then I should be on the permits dashboard page with an ongoing application
    When I select the fee tab and pay the outstanding fees
    Then I am taken to the payment successful page