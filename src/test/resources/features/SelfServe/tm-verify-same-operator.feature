@Business Need: As part of the OTC strategic objects for 2018
@SS-TM-Verify
@SS

Feature: TM signs through verify

  Background:
    #Needs updating to use new application flow
    Given i have a valid "public" licence
    And the TM has successfully signed through verify

  @OLCS-21373
  Scenario: TM has completed journey and is checking Your details section
    Given the operator is on check your answers page
    When you change your details details
    Then the correct headings, data and links should be displayed for Your details section

  Scenario: TM has completed journey and is checking Responsibilities section
    Given the operator is on check your answers page
    When you change your responsibilities details
    Then the correct headings, data and links should be displayed for Responsibilities section

  Scenario: TM has completed journey and is checking Hours per week section
    Given the operator is on check your answers page
    When you change your Hour per week details
    Then the correct headings, data and links should be displayed for Hours per week section

  Scenario: TM has completed journey and is checking Other licences   section
    Given the operator is on check your answers page
    When you change your other licences details
    Then the correct headings, data and links should be displayed for Other licences section

  Scenario: TM has completed journey and is checking Additional information section
    Given the operator is on check your answers page
    When you change your additional information details
    Then the correct headings, data and links should be displayed for Additional information section

  Scenario: TM has completed journey and is checking Other employment section
    Given the operator is on check your answers page
    When you change your other employment details
    Then the correct headings, data and links should be displayed for Other employment section

  Scenario: TM has completed journey and is checking Convictions & Penalties section
    Given the operator is on check your answers page
    When you change your convitions & penalties details
    Then the correct headings, data and links should be displayed for Convictions & Penalties section

  Scenario: TM has completed journey and is checking Revoked, curtailed or suspended licences section
    Given the operator is on check your answers page
    When you change your revoked, curtailed or suspended licences details
    Then the correct headings, data and links should be displayed for Revoked, curtailed or suspended licences section

