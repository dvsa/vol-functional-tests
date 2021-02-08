@SS
@SS-Verify-On
@OLCS-19792
@ss_regression
@gov-verify

Feature: TM signs through verify

  Background:
    And I am the operator and not the transport manager
    And i have a "goods" "GB" partial application

  Scenario: TM who is not Operator / Applicant signs through Verify
    When i add an existing person as a transport manager who is not the operator on "application"
    And i sign the declaration
    And i choose to sign with verify with "pavlov"
    Then the 'Awaiting operator review' post signature page is displayed

  Scenario: TM who is Operator signs through Verify
    When i add an operator as a transport manager
    And i sign the declaration
    And i choose to sign with verify with "pavlov"
    Then the 'Review and declarations' post signature page is displayed

  Scenario: Operator co-signs through Verify
    When i add an existing person as a transport manager who is not the operator on "application"
    And i sign the declaration
    Then i choose to sign with verify with "pavlov"
    And the operator countersigns digitally
    And the 'Review and declarations' post signature page is displayed

  Scenario: Operator co-signs manually
    When i add an existing person as a transport manager who is not the operator on "application"
    And i sign the declaration
    Then i choose to sign with verify with "pavlov"
    And the operator countersigns by print and sign
    And the print and sign page is displayed

  Scenario: Operator rejects TM details and TM details are marked incomplete
    When i add new person as a transport manager and they fill out their details
    And i sign the declaration
    And i choose to sign with verify with "pavlov"
    When the operator rejects the transport managers details
    And the TM has got the reset link email
    And the TM should see the incomplete label and provide details link