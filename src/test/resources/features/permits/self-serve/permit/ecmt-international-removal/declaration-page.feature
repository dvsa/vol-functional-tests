@ecmt_removal
Feature: ECMT International Declaration Page

  Background:
    Given I have a "goods" "standard_international" licence
    And I am on the VOL self-serve site
    And I am on  the ECMT Removal Declaration page

  @EXTERNAL @OLCS-24980 @olcs-28201
  Scenario: Declaration page functionality works as expected
    Then I should see the correct heading on the declaration page
    And  the declaration page has a reference number
    And  the declaration page has correct link under guidance notes
    And  I should see the declaration advisory texts
    Then the declaration page checkbox has the correct text and displayed unselected by default
    When I accept and continue
    Then I should get the correct error message on the declaration page
    When I save and return to overview
    Then I should get the correct error message on the declaration page
    When I click the back link
    Then I should be on the overview page
    And  I click declaration link on the Ecmt removal overview page again
    Then I should be on the declaration page
    When I confirm the declaration
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
    Then I confirm the declaration
    And I accept and continue
    Then I am on the ECMT removal application submitted page

  @EXTERNAL @OLCS-24980 @olcs-27502 @olcs-27581 @OLCS-27781
  Scenario: When fees is paid Declaration page confirmation navigates to submission page
    And I click the back link
    Then I should be on the overview page
    And I click the back link
    Then I should be on the permits dashboard page with an ongoing application
    And I select the fee tab and pay the outstanding fees
    And I select the back to home link
    And I am continuing on the on-going ECMT removal application
    And I click declaration link on the Ecmt removal overview page again
    Then I confirm the declaration
    And I accept and continue
    Then I am on the ECMT removal application submitted page
