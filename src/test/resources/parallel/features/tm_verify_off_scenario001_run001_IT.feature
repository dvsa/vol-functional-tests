# language: en

@Verify-Off
Feature: Countersigning declaration page for the operator (Verify switched off)

@OLCS-21374
Scenario: Verify switched off operator same as TM

Given verify has been switched "off"
When I have a "goods" "GB" partial application
And i add a transport manager
And the transport manager is the owner
Then Signing options are not displayed on the page
And submit to operator button is not displayed

# Source feature: src/test/resources/features/SelfServe/tm-verify-off.feature
# Generated by Cucable

