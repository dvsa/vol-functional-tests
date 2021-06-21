@ecmt_removal @eupa_regression
Feature: ECMT International Declaration Page

  Background:
    Given I have valid Goods standard_international VOL licence
    And I am on the VOL self-serve site
    And I am on  the ECMT Removal Declaration page

  @EXTERNAL @OLCS-24980 @olcs-28201
  Scenario: Declaration page functionality works as expected
    Then the page heading on Ecmt Removal declaration page is displayed correctly
    And  the Ecmt removal declaration page has reference number
    And  the Ecmt removal declaration page has correct link under guidance notes
    And  the advisory text on removal declaration page is displayed correctly
    Then the Ecmt removal declaration page checkbox has the correct text and displayed unselected by default
    When I accept and continue
    Then I should see the validation error message on the Ecmt removal declaration page
    When I save and return to overview
    Then I should see the validation error message on the Ecmt removal declaration page
    When I go back
    Then I should be on the ECMT International Overview Page
    And  I click declaration link on the Ecmt removal overview page again
    Then I am on ECMT Removal Declaration page
    Then I am directed back to the Declaration page
    When I confirm the ECMT removal declaration
    And  I save and return to overview
    Then I am on ECMT removal permits overview page with Declaration section marked as complete
    When I click declaration link on the Ecmt removal overview page again
    And I accept and continue
    Then I am directed to the ECMT removals permit fee page

  @EXTERNAL @OLCS-24980
  Scenario: When fees is waived Declaration page confirmation navigates to submission page
    And I'm viewing my saved ECMT International application in internal
    And I am on the fee details page
    And all fees have been waived
    And I am on the VOL self-serve site
    And I am continuing on the on-going ECMT removal application
    And  I click declaration link on the Ecmt removal overview page again
    Then I confirm the ECMT removal declaration
    And I accept and continue
    Then I am on the ECMT removal application submitted page

  @EXTERNAL @OLCS-24980 @olcs-27502 @olcs-27581 @OLCS-27781
  Scenario: When fees is paid Declaration page confirmation navigates to submission page
    And I go back
    Then I should be on the ECMT International Overview Page
    And I go back
    Then I should be on the permits dashboard page with an ongoing application
    And I select the fee tab and pay the outstanding fees
    And I select the back to home link
    And I am continuing on the on-going ECMT removal application
    And I click declaration link on the Ecmt removal overview page again
    Then I confirm the ECMT removal declaration
    And I accept and continue
    Then I am on the ECMT removal application submitted page
