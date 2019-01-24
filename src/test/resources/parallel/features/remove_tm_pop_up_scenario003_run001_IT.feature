# language: en

@OLCS-19478
@INT
@LAST-TM-POP-UP
Feature: Remove last Transport Manager (TM) pop up

Scenario: Pop up should display new warning message when the last TM is removed from an application

Given i have an application with a transport manager
When the transport manager has been removed by an internal user
Then the remove TM popup should not be displaying new TM remove text

# Source feature: src/test/resources/features/Internal/remove-tm-pop-up.feature
# Generated by Cucable

