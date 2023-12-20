@cookies @ss_regression
Feature: SelfServe Cookies

  Background:
    Given I have a "goods" "standard_international" licence

  @EXTERNAL @OLCS-26828 @OLCS-26908  @reads-and-writes-system-properties
  Scenario: Verify all Cookies
    And I logged into Self Serve site before accepting cookies
    Then I should see the cookies list
    And I accept all cookies from banner
    Then I should see the cookies list
    And I should see no banner in the page

  @EXTERNAL @OLCS-26885  @reads-and-writes-system-properties
  Scenario: Accept all Cookies after login
    And I am on the permit type page before accepting cookies
    And I accept all cookies from banner
    Then I should see the cookies list
    And I should see no banner in the page

  @EXTERNAL @OLCS-26886 @OLCS-26908  @reads-and-writes-system-properties
  Scenario: Cookies preferences Page via set cookies button
    And I should see banner in the page
    And I navigate to cookies details Page
    And I should see no banner in the page

  @EXTERNAL @OLCS-26886  @OLCS-26908  @reads-and-writes-system-properties
  Scenario: Cookies preferences Page via collect info link
    And I should see banner in the page
    And I navigate to cookies details Page from collect info link
    And I should see no banner in the page