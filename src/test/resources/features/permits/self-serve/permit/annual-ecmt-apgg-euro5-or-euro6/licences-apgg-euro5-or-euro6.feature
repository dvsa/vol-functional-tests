@EXTERNAL @ECMT @annual_ecmt_apgg_euro5_or_euro6 @eupa_regression
Feature: Licence page

  Background:
    Given I have a "goods" "standard_international" licence

  @OLCS-21186 @olcs-27581
  Scenario: No default licence selection when there are multiple licences
    When I am on the Annual ECMT licence selection page
    Then There should be no selected licences

  @OLCS-24820 @olcs-27382 @olcs-27581
  Scenario: Not able to apply for a new permit when selected licence has existing application
    And  I have a partial completed ECMT application
    When I try applying with a licence that has an existing annual ECMT application
    Then I should be informed that there is already an active permit application for this licence

  @OLCS-21186  @olcs-27581
  Scenario: User successfully progresses past licences page when saving and continuing
    And I am on the Annual ECMT licence selection page
    When I click save and continue
    Then I will get an error message on the licence page

  @OLCS-24820 @olcs-27581
  Scenario: Back button
    And I am on the Annual ECMT licence selection page
    When I click the back link
    Then I should be taken to the permits dashboard

  @OLCS-21938 @olcs-27581
  Scenario: Type of licence text is displayed next to licences
    And  I am on the Annual ECMT licence selection page
    Then I should see the type of licence next to each licence
