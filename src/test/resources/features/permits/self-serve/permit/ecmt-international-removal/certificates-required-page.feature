@EXTERNAL @ecmt_removal
Feature: ECMT International Removals Certificates required  Page

  Background:
    Given I have a "goods" "standard_national" licence
    And I am on the VOL self-serve site
    And I am on the ECMT Removals certificates required page

  #AC01
  @r222gremovalsfix
  Scenario: Application back button in ECMT Removals certificates required page
    When I go back
    Then I should be on the ECMT International Overview Page

  @r222gremovalsfix
  Scenario: Certificate required page details are displayed correctly
    And  the application reference number is displayed
    And  the page heading should be correct
    And  the advisory texts on certificates required page are displayed correctly
    And  the correct text is displayed next to the checkbox
    When save and continue  button is selected without selecting the checkbox
    Then I should get the correct validation message
    When I save and return to overview without selecting the checkbox
    Then I should get the correct validation message
    When I select the certificate required checkbox
    And  I save and continue
    Then I am on the ecmt removals permit start date page