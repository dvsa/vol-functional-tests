@int_regression
@Complaints-convictions
@OLCS-24339
@FullRegression
@printAndSign

Feature: Public enquiry added and published and deletion of case notes

  Background:
    Given i have a valid "goods" "standard_national" licence
    And I create a new case
    And i have logged in to internal
    When i url search for my licence

  @inquiry
  Scenario: Publish public inquiry
    And i add a new public inquiry
    And i add and publish a hearing
    Then the public inquiry should be published

  Scenario: Delete case note
    And I add notes
    And I delete a case note
    Then the note should be deleted

  @cross-browser
  @Submission
  Scenario: UI - Add a submission
    When i add a submission
    Then the submission details should be displayed


    @PresidingTC
    Scenario: UI - New Transport Manager added to a submission
      When i add a Presiding TC and then create a Submission



  @Create_Complaint
  Scenario: UI - Creating a case with a complaint
    And I navigate to a case
    Then I raise a complaint
    Then the complaint should be displayed

  @Condition_undertaking_case
  Scenario: UI - Add a condition-undertaking to a case
    And I complete the conditions & undertakings form
    Then the condition & undertaking should be displayed

  @Add_Case_Note
  Scenario: UI - Add a new case note
    And I navigate to Notes
    Then I add a Note
    Then the note should be displayed

  @Add_conviction_to_case
  Scenario: UI - Add a conviction to a case
    And I navigate to a case
    And I add conviction to the case
    Then the conviction should be created

