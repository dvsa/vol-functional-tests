@ecmt_removal
Feature: ECMT International Removal Eligiblity page

  Background:
    Given I have a "goods" "standard_international" licence
    And   I am on the VOL self-serve site
    And  I am on the ECMT International Removal Eligibity page

  #AC01

  @EXTERNAL @OLCS-24816
  Scenario:Back Button
    When I click the back link
    Then I should be on the overview page

  @EXTERNAL @OLCS-24816 @r222gremovalsfix
  Scenario: Eligibility page details are displayed correctly
    And  the Application Number is shown correctly on ECMT International Eligibility page
    And  the page heading is shown as per updated AC
    And  the text is shown below the page heading
    And  the text is shown next to the tick box
    And  I save and continue without selecting the checkbox
    And  I save and return to overview without selecting the checkbox
    Then the error message is displayed on ECMT Remove Eligibility Page

  #AC09
  @EXTERNAL @OLCS-24816
  Scenario: The user is taken to Overview page when the 'Save and return to overview' link is selected
    When the checkbox is ticked
    And I save and return to overview
    Then I should be on the overview page