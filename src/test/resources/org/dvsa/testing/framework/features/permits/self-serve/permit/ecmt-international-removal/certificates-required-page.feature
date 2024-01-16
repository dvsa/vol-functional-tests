@EXTERNAL @ecmt_removal @eupa_regression
Feature: ECMT International Removals Certificates required  Page

  Background:
    Given I have a "goods" "standard_national" licence
    And I am on the ECMT Removals certificates required page

  #AC01
  @r222gremovalsfix
  Scenario: Application back button in ECMT Removals certificates required page
    When I click the back link
    Then I should be on the overview page

  @r222gremovalsfix
  Scenario: Certificate required page details are displayed correctly
    And  the application reference number is displayed
    And  the certificates required page heading is as per the AC
    And  the correct text is displayed next to the checkbox
    When save and continue  button is selected without selecting the checkbox
    Then I should get the certificates required page error message
    When I save and return to overview
    Then I should get the certificates required page error message
    When I confirm the Certificates Required checkbox
    Then I am on the ecmt removals permit start date page