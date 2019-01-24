# language: en

@OLCS-20956
@SS
@SS-EXTERNAL-SEARCH
Feature: External user search by Address, Business name, Licence Number and Person's name

Scenario: [Negative]Search for lorry and bus operators by Person's name

Given i have a valid "goods" licence
And I am on the external search page
When I search for a lorry and bus operator by "person"
Then search results page should only display names containing our operator name

# Source feature: src/test/resources/features/SelfServe/external-search.feature
# Generated by Cucable

