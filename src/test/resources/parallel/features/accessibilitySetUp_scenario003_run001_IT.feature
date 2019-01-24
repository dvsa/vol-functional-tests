# language: en

@user
Feature: Set up users for accessibility testing

@tm_accounts
Scenario: PSV account with 16 different licences with external tm

Given I have applied for "standard_national" "goods" TM application
Then accounts should be created

# Source feature: src/test/resources/features/SelfServe/accessibilitySetUp.feature
# Generated by Cucable

