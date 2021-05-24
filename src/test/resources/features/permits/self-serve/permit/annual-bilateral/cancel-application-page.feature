@Deprecated
Feature: Annual Bilateral Application Cancel application page

  Background:
    Given I have a "goods" "standard_international" licence
    And  I am on the VOL self-serve site
    And I'm on bilateral overview page
    And I click cancel application link

  @OLCS-23233 @EXTERNAL @OLCS-26819 @OLCS-27781
  Scenario: Verify Annual Bilateral Cancel application page contents, validation message and back button functionality
    Then I am on the cancel application page for Annual Bilateral page
    Then the application reference number should be displayed above the heading
    Then the bilateral CancelApplication heading should be correct
    Then the bilateral CancelApplication page displays the correct advisory text
    Then I should see the correct text displayed next to the checkbox
    When the cancel application button is selected without checkbox ticked
    When I go back
    Then I should be on the bilateral overview page

  @OLCS-23233 @EXTERNAL @OLCS-26819 @OLCS-27781
  Scenario: Verify Annual Bilateral Cancel  application page , cancel confirmation page contents and functionality
    Then I am on the cancel application page for Annual Bilateral page
    When the checkbox is selected
    And I select cancel application button
    Then I should be taken to cancel confirmation page
    And I select finish button
    Then I should be taken to the permits dashboard

  @EXTERNAL @OLCS-27364 @OLCS-27781
  Scenario: Verify the back button functionality on Cancel application page when clicked from country selection page
     And I go back
     Then I should be on the bilateral overview page
     When I select the edit countries button
     Then I am on the Bilaterals country selection page
     When I click cancel application link for bilateral application
     Then I am on the cancel application page for Annual Bilateral page
     When I go back
     Then I am on the Bilaterals country selection page

  @EXTERNAL @OLCS-27364
  Scenario: Verify the back button functionality on Cancel application page for Bilateral cabotage permits only , when clicked from Cabotage page
    When I go back
    Then I should be on the bilateral overview page
    And  I navigate to the Bilaterals cabotage page
    When I select 'no' button
    And I click save and continue on cabotage page
    When I go back
    Then I am navigated to cabotage page