@EXTERNAL @annual_ecmt_apgg_euro5_or_euro6 @ECMT  @eupa_regression
Feature: Certificates required Page

  Background:
    Given I have a "goods" "standard_international" licence
    And  I am on the VOL self-serve site
    And I am on the Ecmt Certificates required Page

  @OLCS-24977 @olcs-27581 @OLCS-28275
    Scenario: Check that page  functionality and contents are as per the AC
    And The application reference is displayed on the page
    And the certificates required page heading is as per the AC
    And the advisory texts on certificates required page are displayed
    And The advisory text contains bold characters at the right places
    And There is one checkbox with right label and not checked by default
    And  if I don't select the checkbox and click Save and Continue button
    Then I should get the certificates required page error message
    And  if I don't select the checkbox and click Save and Return to Overview button
    Then I should get the certificates required page error message
    And  if I select the checkbox and click Save and Return to Overview button
    Then  I should be on the Annual ECMT overview page

  @OLCS-24977 @OLCS-28275
    Scenario: Verify that save and continue button takes me to the restricted countries page when the checkbox on the page is selected
    When I select the checkbox and click Save and Continue button
    Then I am taken to the Restricted countries page

  @OLCS-24977 @OLCS-28275
  Scenario: Check that back button functions correctly on the certificates required page
    When I click the back link
    Then I should be on the Annual ECMT overview page