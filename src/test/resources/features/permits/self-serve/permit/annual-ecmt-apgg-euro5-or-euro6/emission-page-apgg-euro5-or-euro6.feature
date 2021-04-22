@EXTERNAL @annual_ecmt_apgg_euro5_or_euro6
Feature: ECMT Permit Euro Emission Standard Page

  Background:
    Given I have a valid Goods standard_international VOL licences
    And I am on the VOL self-serve site

  @EXTERNAL @OLC-20557,@OLCS-24818 @ECMT @Test2 @olcs-27581 @OLCS-28275 @WIP
  Scenario Outline: Vehicles meeting Euro6 standards successfully
    Given I have a valid Goods <licence_type> VOL licences
    And I am on the euro emission standard page
    When I select the checkbox declaration
    Then I should be able to navigate to the next page

    Examples:
      | licence_type                |
      | restricted                  |
      | standard_international      |

  @EXTERNAL @OLC-20557,@OLCS-24818 @ECMT @Test2 @olcs-27581 @OLCS-28275
  Scenario: Successful navigation of back link
    And I am on the euro emission standard page
    When I select the back hyperlink
    Then should see the overview page without updating any changes

  @EXTERNAL @OLC-20557,@OLCS-24818 @ECMT @Test2 @olcs-27581 @OLCS-28275
  Scenario: Fails validation when saving and returning to overview
    And I am on the euro emission standard page
    Then I see the application reference number is displayed correctly
    And  the texts are displayed correctly
    When I save and continue
    Then I should see the validation errors for euro 6 page
    When I select save and return overview link
    Then I should get an error message
    And I select the emission checkbox
    When I select save and return overview link
    Then I should see the overview page with updated changes