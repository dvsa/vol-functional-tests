@lgv
Feature: Caseworkers can apply for lgv only and mixed fleet licences and are redirected to the correct application overview

  Scenario: Caseworker applying for a Goods SI is prompted to enter lgv only or not
    Given I create a new external user
    And i have logged in to internal
    When a caseworker goes to apply for a goods standard_international licence
    Then i am prompted with the choice of LGV Mixed and LGV Only applications

  Scenario: Caseworker applying for a NI Goods SI is prompted to enter lgv only or not
    Given I create a new NI external user
    And i have logged in to internal
    When a caseworker goes to apply for a goods standard_international licence
    Then i am prompted with the choice of LGV Mixed and LGV Only applications

  Scenario: Caseworker applying for a lgv only application without confirming the lgv only declaration results in an error
    Given I create a new external user
    And i have logged in to internal
    And a caseworker goes to apply for a goods standard_international licence
    When i choose to have light goods vehicles only and click create without confirming the declaration
    Then A LGV undertakings error message should be displayed

  Scenario: Caseworker clicks cancel when creating a new application and is reverted to the previous screen and the modal disappears
    Given I create a new NI external user
    And i have logged in to internal
    And a caseworker goes to apply for a goods standard_international licence
    When I click cancel
    Then the modal box is hidden
    And the caseworker is still on the operators page

  Scenario: Caseworker who tries to create a Goods SI application but doesn't choose lgv only or mixed vehicles is prompted with an error
    Given I create a new external user
    And i have logged in to internal
    And a caseworker goes to apply for a goods standard_international licence
    When i click submit
    Then A LGV only error message should be displayed

  @int_regression
  Scenario: Caseworker clicks yes to lgv only and clicks create is redirected to lgv only application overview and type of licence is marked complete
    Given I create a new external user
    And i have logged in to internal
    And a caseworker goes to apply for a goods standard_international licence
    When i choose to have light goods vehicles only and click create
    Then the caseworker is navigated to the lgv only application overview
    And the type of licence section is marked as complete

   @smoketest
  Scenario: Caseworker clicks no to lgv only and clicks create is redirected to mixed fleet application overview and type of licence is marked complete
    Given I create a new external user
    And i have logged in to internal
    And a caseworker goes to apply for a goods standard_international licence
    When i choose to have mixed vehicles and create
    Then the caseworker is navigated to the lgv mixed application overview
    And the type of licence section is marked as complete
