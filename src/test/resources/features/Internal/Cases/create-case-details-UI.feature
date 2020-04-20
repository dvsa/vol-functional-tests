@INT
@OLCS-24339

Feature: Public enquiry added and published and deletion of case notes

  Background:
    Given i have a valid "goods" "standard_national" licence
    And I create a new case
    And i have logged in to internal
    When i url search for my licence

  Scenario: Publish public inquiry
    And i add a new public inquiry
    And i add and publish a hearing
    Then the public inquiry should be published

  Scenario: Delete case note
    And I add notes
    And I delete a case note
    Then the note should be deleted