@int_regression
@OLCS-24339

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

  @Create_Complaint
  Scenario: UI - Creating a case with a complaint
    And I select a case to raise a complaint
    Then Details should fill in the complaint form
    And I save the form

  @Condition_undertaking_case
  Scenario: UI - Add a condition-undertaking to a case
    And I create a new case
    Then Select a case to create new case for adding a condition-undertaking
    And I add new case details
    Then I save the form
    And submit the Condition and Undertaking form

  @Add_Case_Note
  Scenario Outline: UI - Add a new case note
    And select a "<NoteType>" to complete all forms by clicking add the button
    Then I save the form

  Examples:
    | NoteType          |
    | Application       |
    | Bus Registration  |
    | Case              |
    | Licence           |
    | Permit            |
    | Transport Manager |

  @Add_conviction_to_case
  Scenario: UI - Add a conviction to a case
    Then I search for the case before adding conviction
    And add conviction to the case






