# language: en

@user
Feature: Set up users for accessibility testing

@multiple_licences
Scenario: PSV account with 16 different licences and 3 vehicles

Given I have applied for "standard_international" "public" licences
When i have logged in to self serve
Then the licence should be created and granted

# Source feature: src/test/resources/features/SelfServe/accessibilitySetUp.feature
# Generated by Cucable

