@Surrender
@sign-surrender
@gov-sign-in

Feature: Sign surrender

  Background:
    Given i have a valid "goods" "standard_international" licence
    And i choose to surrender my licence

    Scenario: Sign with gov sign in - check that surrender has been created
      When I can navigate to gov sign in
      And I sign in to gov sign in to complete the process
      Then the post gov sign in page is displayed
      And the surrender status is "Surrender under consideration"