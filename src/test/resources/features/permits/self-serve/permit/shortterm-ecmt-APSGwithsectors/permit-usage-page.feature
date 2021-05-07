@Deprecated
Feature: Short term ECMT APSG with sectors permit usage page

Background:
  Given I have valid Goods standard_international VOL licence
  And  I am on the VOL self-serve site
  And I am on the shortterm permit usage page

  @OLCS-25130 @OLCS-25483 @shortterm
  Scenario: Check that Short term permits Usage page  functionality and contents are as per the AC
    Then the shortterm ecmt permit usage page has an application reference number
    And the page heading on the permit usage page is displayed correctly
    And the short term ecmt permit usage buttons are displayed  unselected by default
    And the shortterm ecmt permit usage page has advisory messages
    And when I save and continue without selecting any radio button
    Then I should get error message on the permit usage page
    And when I save and return to overview without selecting any radio button
    Then I should get error message on the permit usage page
    When I go back
    Then I should be on the short term ECMT overview page

  @EXTERNAL @OLCS-25130
  Scenario: Save and continue after confirming usage navigates to next page
    When I confirm the permit usage
    Then the user is navigated to the next page

  @EXTERNAL @OLCS-25130
  Scenario: Save and Return to Overview after confirming navigates to the Overview page
    When I confirm the permit usage
    And I select save and return overview link
    Then the user is navigated to the overview page with the permits usage section status displayed as completed