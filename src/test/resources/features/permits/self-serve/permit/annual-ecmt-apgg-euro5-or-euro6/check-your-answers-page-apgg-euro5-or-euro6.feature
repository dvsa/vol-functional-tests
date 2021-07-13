@EXTERNAL @ECMT @annual_ecmt_apgg_euro5_or_euro6
Feature:Check your answers page

  Background:
    Given I have a "goods" "standard_international" licence
    And I am on the VOL self-serve site
    And I am on the application overview page
    And I have completed all steps prior to check your answers page

  @OLCS-21128 @OLCS-24973 @olcs-27581
  Scenario: The correct information is displayed
    Then the information I inserted during the application is displayed

  @OLCS-21128 @OLCS-24973 @olcs-27581
  Scenario Outline: Able to change sections
    When I change the <section>

    Examples:
    | section                             |
    | Euro6                               |
    | Cabotage                        |
    | RestrictedCountries                 |

  @OLCS-21128 @OLCS-24973 @olcs-27581
  Scenario: Return to overview
    When I save and return to overview
    Then I should be on the Annual ECMT overview page

  @OLCS-21128 @OLCS-24973
  Scenario: Application cancel button
    When I click the back link
    Then I should be on the Annual ECMT overview page

  @OLCS-21281 @OLCS-24973 @olcs-27581
  Scenario Outline: Needs to redo check your answers after changing one of the sections
    When I edit <section> and apply the changes
#    add check to change?

        # Note: This values need to match the literal values within the ApplicationInfo enum
    Examples:
      | section                             |
      | Cabotage                            |
      | RestrictedCountries                 |
