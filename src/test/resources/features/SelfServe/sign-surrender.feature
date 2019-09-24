@SS
@Surrender
@gov-verify

Feature: Sign surrender

  Background:
    Given i have a valid "goods" "sn" licence
    And i choose to surrender my licence

  @ss_regression
  #Tag with  verify?
  Scenario: Sign with verify - check that surrender has been created
    When i sign with verify
    Then the post verify success page is displayed
    And the surrender status is "Surrender under consideration"

  Scenario: Print and sign - check that surrender has been created
    When the user chooses to print and sign
    Then the surrender print and sign page is displayed
    And the surrender status is "Surrender under consideration"