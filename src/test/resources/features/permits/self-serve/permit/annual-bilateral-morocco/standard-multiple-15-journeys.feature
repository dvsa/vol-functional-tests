@bilateral_morocco @eupa_regression
Feature: Bilaterals Morocco Application Path for standard multiple 15 journeys

  Background:
    Given I have valid Goods standard_international VOL licence
    And   I am on the VOL self-serve site
    And  I have selected Morocco and I am on the Bilateral application overview page

  @OLCS-28178
  Scenario: To verify application is submitted successfully
    When I submit the application on selection of Morocco link on overview page
    Then the status of Morocco under answers questions for individual countries section is marked as Completed
    And  the status of answers questions for individual countries as complete
    When I accept declaration and submit the application
    Then I am on the Annual Bilateral application submitted page with correct information and content
    When I click 'go to permits' dashboard on the submitted page
    Then  the application is under issued permits table with status as valid
    When I am viewing an issued annual bilateral permit on self-serve
    And  I select return to permits dashboard hyperlink
    Then I am navigated back to permits dashboard page
