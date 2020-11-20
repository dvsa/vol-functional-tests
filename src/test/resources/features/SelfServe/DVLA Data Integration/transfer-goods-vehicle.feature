#https://jira.dvsacloud.uk/browse/VOL-90 and https://jira.dvsacloud.uk/browse/VOL-91

@VOL-90
@VOL-91
Feature: Transfer a vehicle

  Background:
    Given I have applied for "standard_national" "goods" licences
    When I navigate to manage vehicle page
    And choose to transfer a vehicle
