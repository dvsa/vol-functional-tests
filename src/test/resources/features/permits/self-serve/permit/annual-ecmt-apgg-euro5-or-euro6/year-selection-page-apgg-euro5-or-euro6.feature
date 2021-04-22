@annual_ecmt_apgg_euro5_or_euro6
Feature: Annual Ecmt Year Selection Page

  Background:
    Given  I have valid Goods standard_international VOL licence
    And  I am on the VOL self-serve site
    And I am on the Year Selection Page

  @EXTERNAL @OLCS-24972 @ECMT @Test3
  Scenario: Application Back button
    When I go back
    Then the user is navigated to the permit type page

  @EXTERNAL @OLCS-24972 @ECMT @Test3
  Scenario: Page Heading and advisory messages are displayed correctly
    Then the page heading on Annual Ecmt Year selection page is displayed correctly
    And the validity error message is displayed
    When I confirm  the year selection
    Then the user is navigated to licence selection page