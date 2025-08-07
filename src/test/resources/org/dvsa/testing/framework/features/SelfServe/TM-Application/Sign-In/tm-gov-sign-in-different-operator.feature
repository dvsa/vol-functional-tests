@gov-sign-in
@tm-application-gov
#@ss_regression
@FullRegression

Feature: TM signs through gov-sign-in

  Background:
    Given i have a "goods" "GB" partial application
    And I am the operator and not the transport manager

  @tm-not-operator
  Scenario: TM who is not Operator_Applicant signs through gov sign in
    When i add an existing person as a transport manager who is not the operator on "application"
    And i sign the declaration
    Then I can navigate to gov sign in
    And I sign in to gov sign in to complete the process
    Then the VOL 'Awaiting operator review' post signature page is displayed

  @tm-operator
  Scenario: TM who is Operator signs through gov sign in
    When i add an operator as a transport manager
    And i sign the declaration
    Then I can navigate to gov sign in
    And I sign in to gov sign in to complete the process
    And the VOL 'Review and declarations' post signature page is displayed

  @operator-cosigns
  Scenario: Operator co-signs through gov sign in
    When i add an existing person as a transport manager who is not the operator on "application"
    And i sign the declaration
    Then I can navigate to gov sign in
    And I sign in to gov sign in to complete the process
    And the operator countersigns digitally
    And the VOL 'Review and declarations' post signature page is displayed

  @tm-operator-cosigns-manually
  Scenario: Operator co-signs manually
    When i add an existing person as a transport manager who is not the operator on "application"
    And i sign the declaration
    Then I can navigate to gov sign in
    When I sign in to gov sign in to complete the process
    And the operator countersigns by print and sign
    And the print and sign page is displayed

  @tm-operator-rejects-details
  Scenario: Operator rejects TM details and TM details are marked incomplete
    When i add new person as a transport manager and they fill out their details
    And i sign the declaration
    Then I can navigate to gov sign in
    When I sign in to gov sign in to complete the process
    When the operator rejects the transport managers details
    And the TM has got the reset link email
    And the TM should see the incomplete label and provide details link