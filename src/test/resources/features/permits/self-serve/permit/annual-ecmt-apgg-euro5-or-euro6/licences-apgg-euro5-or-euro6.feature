@annual_ecmt_apgg_euro5_or_euro6
Feature: Licence page

  Background:
    Given I have a "goods" "standard_international" licence
    Given I am on the VOL self-serve site

  # @OLCS-20697 -> @OLCS-21186
  @EXTERNAL @OLCS-21186 @ECMT @Test2 @olcs-27581
  Scenario: No default licence selection when there are multiple licences
    When I am on the Annual ECMT licence selection page
    Then There should be no selected licences

  @EXTERNAL @OLCS-24820 @Deprecated
  Scenario: Single licence is selected by Default
    When I am on the Annual ECMT licence selection page
    Then the licence number should be selected

  @EXTERNAL @OLCS-24820 @ECMT @olcs-27382 @Test2 @olcs-27581
  Scenario: Not able to apply for a new permit when selected licence has existing application
    And  I have a partial completed ECMT application
    When I try applying with a licence that has an existing annual ECMT application
    Then I should be informed that there is already an active permit application for this licence

  @EXTERNAL @OLCS-21186 @ECMT @Test2 @olcs-27581
  Scenario: User successfully progresses past licences page when saving and continuing
    And I am on the Annual ECMT licence selection page
    When I save and continue
    Then I will get an error message on the licence page
    When  I select any licence number
    Then the user is navigated to the next page

  @EXTERNAL @OLCS-24820 @ECMT @Test2 @olcs-27581
  Scenario: Back button
    And I am on the Annual ECMT licence selection page
    When I click the back link
    Then I should be taken to the permits dashboard

  @EXTERNAL @OLCS-21461 @Deprecated
  Scenario: Applied against all licences
    And I have completed an ECMT application
    When I try applying for an annual ECMT again
    Then I should be informed that there is already an active permit application for this licence

  @EXTERNAL @OLCS-21938 @ECMT @Test2 @olcs-27581
  Scenario: Type of licence text is displayed next to licences
    And  I am on the Annual ECMT licence selection page
    Then I should see the type of licence next to each licence
#    If fails, it did pass in "licences" via a scenario outline but I don't know what it would have changed.
