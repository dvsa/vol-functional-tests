@cookies
Feature: SelfServe Cookies

  Background:
    Given I have a "goods" "standard_international" licence
    And I am on the VOL self-serve site

  @EXTERNAL @OLCS-26828 @OLCS-26908
  Scenario: Verify all Cookies
    And I logged into Self Serve site before accepting cookies
    Then I should see the cookies list
    And I accept all cookies from banner
    Then I should see the cookies list
    And I should see no banner in the page

  @EXTERNAL @OLCS-26885
  Scenario: Accept all Cookies after login
    And I am on the permit type page before accepting cookies
    And I accept all cookies from banner
    Then I should see the cookies list
    And I should see no banner in the page

  @EXTERNAL @OLCS-26886 @OLCS-26908
  Scenario: Cookies preferences Page via set cookies button
    And I should see banner in the page
    And I navigate to cookies details Page
    And I should see no banner in the page

  @EXTERNAL @OLCS-26886  @OLCS-26908
  Scenario: Cookies preferences Page via collect info link
    And I should see banner in the page
    And I navigate to cookies details Page from collect info link
    And I should see no banner in the page

  @EXTERNAL @OLCS-26885 @WIP
  Scenario: Delete all Cookies after login
    And I logged into Self Serve site before accepting cookies
    And I accept all cookies from banner
    Then I should see the cookies list
    And I should see no banner in the page
    And I delete all cookies
    And I should be on login page

  @EXTERNAL @OLCS-26885 @WIP
  Scenario: Delete Token Cookies after login
    And I logged into Self Serve site before accepting cookies
    And I accept all cookies from banner
    Then I should see the cookies list
    And I should see no banner in the page
    And I delete token cookies
    And I should be on login page

  @EXTERNAL @OLCS-26885 @WIP
  Scenario: Dont Accept Cookies
    And I should see banner in the page
    And I logged into Self Serve site before accepting cookies
    And I should see banner in the page
    Then I should see the cookies list
    And I should see the same cookies list in Permit Page
    And I should see banner in the page
    And I should see the same cookies list in ECMT Removal Check Answers Page
    And I should see banner in the page
    And I should see the same cookies list in ECMT Removal valid Page
    And I should see banner in the page

  @EXTERNAL @OLCS-26885 @WIP @OLCS-26908
  Scenario: Accept all Cookies Before login
    And I accept all cookies from banner
    Then I should see the cookies list
    And I should see no banner in the page
    And I logged into Self Serve site before accepting cookies
    And I should see the same cookies list in Permit Page
    And I should see no banner in the page
    And I should see the same cookies list in ECMT Removal Check Answers Page
    And I should see no banner in the page

  @EXTERNAL @OLCS-26887 @WIP @OLCS-26908
  Scenario: Opt out google analytics Cookies
    And I should see banner in the page
    And I Should see google analytics cookies
    And I navigate to cookies details Page from collect info link
    And I should see no banner in the page
    And I Should see google analytics cookies
    And I Opt out google analytics cookies

  @EXTERNAL @OLCS-26887 @WIP
  Scenario: Opt In google analytics Cookies
    And I am on the VOL self-serve site with cookies
    And I should see banner in the page
    And I navigate to cookies details Page from collect info link
    And I should see no banner in the page
    And I Opt in google analytics cookies
    Then I should see the cookies list
    And I Should see All google analytics cookies

  @EXTERNAL @OLCS-26887 @WIP
  Scenario: Opt In User choice Cookies
    And I am on the VOL self-serve site with cookies
    And I should see banner in the page
    And I navigate to cookies details Page from collect info link
    And I should see no banner in the page
    And I Opt out choice cookies
    And I Opt In choice cookies
    Then I should see the cookies list
    And I Should see settings cookies