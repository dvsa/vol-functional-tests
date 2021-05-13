@EXTERNAL @annual_ecmt_apgg_euro5_or_euro6
Feature:Check your answers page

  Background:
    Given I have a "goods" "standard_international" licence
    And I am on the VOL self-serve site
    And  I am on the application overview page
    And I have completed all steps prior to check your answers page

  @EXTERNAL @OLCS-21128 @ECMT @OLCS-24973 @Test2 @olcs-27581
  Scenario: The correct information is displayed
    Then the information I inserted during the application is displayed
    When I confirm and continue
    Then I should be taken to the next section

  @EXTERNAL @OLCS-21128 @ECMT @OLCS-24973 @Test2 @olcs-27581
  Scenario Outline: Able to change sections
    When I change the <section>
    Then I should be taken to the <section> page

    # Note: This values need to match the literal values within the ApplicationInfo enum
    Examples:
    | section                             |
    #| Licence                             | -- Licence section no longer visible on the overview page
    | Euro6                               |
    | Cabotage                            |
    | RestrictedCountries                 |

  @EXTERNAL @OLCS-21128 @ECMT @OLCS-24973 @Test2 @olcs-27581
  Scenario: Return to overview
    When I save and return to overview
    Then I should be on the Annual ECMT overview page

  @EXTERNAL @OLCS-21128 @ECMT @OLCS-24973 @Test2
  Scenario: Application cancel button
    When I go back
    Then I should be on the Annual ECMT overview page

  @OLCS-21281 @EXTERNAL @ECMT @OLCS-24973 @Test2 @olcs-27581
  Scenario Outline: Needs to redo check your answers after changing one of the sections
    When I edit <section> and apply the changes
    Then I should be taken to the next section

        # Note: This values need to match the literal values within the ApplicationInfo enum
    Examples:
      | section                             |
      #| Licence                             | -- Licence section no longer visible on the overview page
      | Cabotage                            |
      | RestrictedCountries                 |
      #| ProportionOfInternationalBusiness   |  -- scoring flow is no longer used
      #| Sector                              |  -- scoring flow is no longer used
