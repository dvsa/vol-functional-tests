@SS
@SS-LAST-TM-TRIGGER
@OLCS-19479
@ss_regression

Feature: Set and check criteria for triggering automatic letter

  Background:
    Given i have a valid "public" "si" licence

  Scenario: Generate letter for valid licence when ss removes last TM
    When a self-serve user removes the last TM
    Then a flag should be set in the DB

    # May get around to doing but requires modification of database date values.
#  Scenario: No letter generated when ss adds a TM
#    When i have logged in to self serve
#    And i remove the last transport manager on self serve
#    And i initiate a variation by adding a transport manager
#    And i sign the declaration
#    And i choose to sign with verify with "pavlov"
#    And i navigate to the review and declarations page and submit the application
#    And i have logged in to internal
#    And i url search for my application
#    And the caseworker completes the tracking and grants the application

  Scenario: TM verifies variational not as operator
    When i have logged in to self serve
    And i add an existing person as a transport manager who is not the operator
    And i sign the declaration
    And i choose to sign with verify with "pavlov"
    And the operator countersigns digitally
    Then the 'Review and declarations' post signature page is displayed






