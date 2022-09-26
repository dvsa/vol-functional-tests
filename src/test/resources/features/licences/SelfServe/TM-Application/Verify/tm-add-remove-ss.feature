@SS-LAST-TM-TRIGGER
@OLCS-19479
@gov-verify

Feature: Set and check criteria for triggering automatic letter

  Background:
    Given i have a valid "public" "standard_international" licence

  @ss_regression
  Scenario: Generate letter for valid licence when ss removes last TM
    When a self-serve user removes the last TM
    Then a pop up should be displayed advising the user that they are about to remove the last TM

  @tm-application
  @cross-browser
  Scenario: TM verifies variational not as operator
    When i add an existing person as a transport manager who is not the operator on "variation"
    And i sign the declaration
    And i choose to sign with verify
    And the operator countersigns digitally
    Then the 'Review and declarations' post signature page is displayed