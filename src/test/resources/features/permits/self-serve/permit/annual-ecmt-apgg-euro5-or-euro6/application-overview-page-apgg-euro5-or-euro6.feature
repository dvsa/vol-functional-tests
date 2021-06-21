@EXTERNAL @annual_ecmt_apgg_euro5_or_euro6 @eupa_regression
Feature: Application overview page

  Background:
    Given I have valid Goods standard_international VOL licence
    And  I am on the VOL self-serve site
    And I am on the application overview page

  @EXTERNAL @ECMT @OLCS-24821
  Scenario Outline: Specific sections are disabled if there's a preceding step that's incomplete
    Then the <section> section should be disabled

    Examples:
    | section            |
    | check your answers |
    | declaration        |


  @EXTERNAL @ECMT @OLCS-24821 @Test1 @olcs-27581
  Scenario: Check your answers is enabled if all preceding steps are completed
    When I fill all steps preceding steps to check your answers
    And I use the application back button
    Then the check your answers section should be enabled

  @EXTERNAL @ECMT @OLCS-24821 @Test1 @olcs-27581
  Scenario: Declaration is enabled if all preceding steps are completed
    When I fill all steps preceding steps to declaration
    And I use the application back button
    Then the declaration section should be enabled

  @EXTERNAL @ECMT @OLCS-24821 @Test1 @olcs-27581
  #AC03
  Scenario: Application overview functionalities are displayed correctly
    Then only the expected status labels are displayed
    And  the page heading is displayed correctly
    #And  the advisory text is displayed correctly ---- advisory text no longer displayed on overview page
    #And  the guidance on permits link navigates to the correct link  --- guidance link no longer displayed on the overview page
    When I use the application back button
    Then I should be taken to the permits dashboard