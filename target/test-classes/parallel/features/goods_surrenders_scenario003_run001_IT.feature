# language: en

@OLCS-21939
Feature: Digital Surrender - Goods Surrender licence

Scenario: Check current discs

Given i have a valid "goods" licence
And i choose to surrender my licence
When i am on the review contact details page
And discs have been added to my licence
And i navigate to the current discs page
Then the number of disc should match the vehicles registered on the licence

# Source feature: src/test/resources/features/SelfServe/goods-surrenders.feature
# Generated by Cucable

