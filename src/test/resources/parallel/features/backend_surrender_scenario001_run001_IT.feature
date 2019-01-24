# language: en

@OLCS-22443
Feature: Surrendering a licence

Scenario: internal user can create, update and delete a licence surrender

Given i have a valid "public" licence
Then as "internal" user I can surrender a licence
And as "internal" user I can update surrender details
And as "internal" user I cannot surrender a licence again
And as internal user i can delete a surrender

# Source feature: src/test/resources/features/Internal/backend-surrender.feature
# Generated by Cucable

