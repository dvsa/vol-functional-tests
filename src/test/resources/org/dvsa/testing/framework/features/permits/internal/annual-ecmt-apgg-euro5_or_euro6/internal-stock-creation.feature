@smoketest @eupa_regression @localsmoke
Feature: Annual ECMT APGG Euro 5 or Euro 6 stock creation

  Background:
  Given i have an admin account to add users

 @OLCS-28353  @OLCS-28261
  Scenario: Admin internal caseworker creates ECMT APGG Euro 5 or Euro 6 stock successfully
   When I log in as an internal user with admin privileges
   Then I should be able to see Permits option
   And I add a new ECMT APGG Euro 5 or Euro 6 stock