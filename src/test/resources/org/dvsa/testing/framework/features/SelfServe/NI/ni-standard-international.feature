@ss_regression
@vol763 @ni-standard-international-persistence
Feature: Northern Ireland standard international application

  Scenario Outline: Save and reopen a new application for an NI operator
    Given an operator has an outstanding Northern Ireland application
    When the operator starts a "Northern Ireland" "Standard International" application with light goods vehicles only "<choice>" and undertaking confirmation <confirmed>
    Then the "Apply for a new licence" overview allows reopening the saved "Northern Ireland" "Standard International" application with light goods vehicles only "<choice>" and undertaking confirmation <confirmed>

    Examples:
      | choice | confirmed |
      | Yes    | true      |
      | No     | false     |
