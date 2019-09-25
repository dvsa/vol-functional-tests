@SS
@SS-LAST-TM-TRIGGER
@OLCS-19479
@ss_regression
@gov-verify

Feature: Set and check criteria for triggering automatic letter

  Background:
    Given i have a valid "public" "si" licence

  Scenario: Generate letter for valid licence when ss removes last TM
    When a self-serve user removes the last TM
    Then a flag should be set in the DB

  @apiBreak
  Scenario: TM verifies variational not as operator
    When i add an existing person as a transport manager who is not the operator on "variation"
    And i sign the declaration
    And i choose to sign with verify with "pavlov"
    And the operator countersigns digitally
    Then the 'Review and declarations' post signature page is displayed