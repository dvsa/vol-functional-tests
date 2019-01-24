# language: en

@OLCS-13203
@INT
@INTERIM
Feature: Change Validation On Interim Vehicle Authority

Scenario: Interim Vehicle Authority Less than Application Vehicle Authority

Given i have a valid "goods" licence
And i have logged in to internal
And i search for my licence
When I have an interim vehicle authority less than my application vehicle authority
Then I should be able to save the application without any errors

# Source feature: src/test/resources/features/Internal/interim-licence.feature
# Generated by Cucable

