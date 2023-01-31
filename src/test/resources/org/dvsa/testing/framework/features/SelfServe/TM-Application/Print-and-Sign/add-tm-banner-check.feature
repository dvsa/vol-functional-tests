@OLCS-21390
Business Need: A TM should no longer be able to access a download link to the TM1 form from VOL, or expect to have a form sent to them via email from VOL.

Feature: Remove reference to TM1 form

  Background:
    Given i have a valid "goods" "standard_national" licence
    And i have logged in to self serve

  Scenario: Update text to remove any reference to the form
    When i add a new transport manager
    Then a transport manager has been created banner is displayed

  Scenario: Check that link is not displayed on details page
    When i add a new transport manager
    Then the download TM1 for should not be displayed on the details page