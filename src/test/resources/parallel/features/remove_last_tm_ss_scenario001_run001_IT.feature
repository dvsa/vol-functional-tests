# language: en

@SS
@SS-LAST-TM-TRIGGER
@OLCS-19479
Feature: Set and check criteria for triggering automatic letter

Scenario: Generate letter for valid licence when ss removes last TM

Given i have a valid "public" licence
When a self-serve user removes the last TM
Then a flag should be set in the DB

# Source feature: src/test/resources/features/SelfServe/remove-last-tm-ss.feature
# Generated by Cucable

