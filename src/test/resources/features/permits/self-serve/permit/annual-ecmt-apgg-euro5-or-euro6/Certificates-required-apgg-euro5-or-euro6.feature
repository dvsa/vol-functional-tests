@EXTERNAL @annual_ecmt_apgg_euro5_or_euro6 @eupa_regression
Feature: Certificates required Page

  Background:
    Given I have valid Goods standard_international VOL licences
    And  I am on the VOL self-serve site
    And I am on the Ecmt Certificates required Page

  @External @OLCS-24977 @ECMT @Tets1 @olcs-27581 @OLCS-28275
    Scenario: Check that page  functionality and contents are as per the AC
    And The application reference is displayed on the page
    And The main page heading is as per the AC
    #And The guidance notes link text is correct and links to the correct url -- guidance notes link removed ----
    And Correct advisory text is shown below the page heading
    And The advisory text contains bold characters at the right places
    And There is one checkbox with right label and not checked by default
    And  if I don't select the checkbox and click Save and Continue button
    Then I am presented with a validation error message
    And  if I don't select the checkbox and click Save and Return to Overview button
    Then I am presented with same validation error message
    And  if I select the checkbox and click Save and Return to Overview button
    Then  I should be on the Annual ECMT overview page

  @External @OLCS-24977 @ECMT @Test1 @OLCS-28275
    Scenario: Verify that save and continue button takes me to the restricted countries page when the checkbox on the page is selected
    When I select the checkbox and click Save and Continue button
    Then I am taken to the Restricted countries page

  @External @OLCS-24977 @ECMT @Test1 @OLCS-28275
  Scenario: Check that back button functions correctly on the certificates required page
    When I go back
    Then I should be on the Annual ECMT overview page