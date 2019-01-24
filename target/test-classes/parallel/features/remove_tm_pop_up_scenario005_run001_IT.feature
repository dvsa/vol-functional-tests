# language: en

@OLCS-19478
@INT
@LAST-TM-POP-UP
Feature: Remove last Transport Manager (TM) pop up

Scenario: Pop up should not displayed when removed from a variation

Given i have an application with a transport manager
Given the licence has been granted
When i create a variation
And i update the licence type
And the transport manager has been removed by an internal user
Then the remove TM popup should not be displaying new TM remove text

# Source feature: src/test/resources/features/Internal/remove-tm-pop-up.feature
# Generated by Cucable

