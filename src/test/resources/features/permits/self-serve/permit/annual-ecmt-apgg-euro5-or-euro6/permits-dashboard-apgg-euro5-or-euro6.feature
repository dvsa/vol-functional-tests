@annual_ecmt_apgg_euro5_or_euro6
Feature: Self-serve dashboard

  Background:
    Given I have a "goods" "standard_international" licence
    And I am on the VOL self-serve site

  @EXTERNAL @OLCS-26301 @ECMT @Test3
  Scenario: Information Heading and Text appears correct
    Given I am on the permits dashboard on external
    Then Information and Text appear correctly

  @EXTERNAL @OLCS-21112 @ECMT @olcs-27382 @Test3
  Scenario: Displays all ECMT permit applications
    And I have completed an ECMT application
    And I view the application from ongoing permit application table
    Then the user is navigated to under consideration page
    # TODO: look into fixing above corresponding step def, ideally backend should be used in place of UI were possible.

  @EXTERNAL @OLCS-25664 @ECMT @Test3
  Scenario: Selection of Not yet Submitted application navigates to application overview page
    And I have a partial completed ECMT application
    When I view the application from ongoing permit application table
    Then I am on the annual ECMT application overview page

  @EXTERNAL @OLCS-25664 @OLCS-28275
  Scenario: Selection of awaiting fee ECMT application navigates to awaiting fee page
    And  I have an annual ECMT application in awaiting fee status
    When I view the application from ongoing permit application table
    Then the user is navigated to awaiting fee page

  @EXTERNAL @OLCS-21112 @WIP
  Scenario: ECMT permit applications are sorted in by permit id descending order
    And I have completed an ECMT application
   Then ongoing permits should be sorted by permit ID in descending order

  @EXTERNAL @OLCS-21112 @ECMT @WIP
  Scenario: Only displays ECMT permits with the correct status
    And I have completed an ECMT application
    Then only ECMT applications with the right status are displayed

  @OLCS-23131 @EXTERNAL @OLCS-21112 @Deprecated
  Scenario: Issued annual bilateral displayed in reference descending order
    And I have completed all annual bilateral application
    Then issued permits should be sorted by reference number in descending order