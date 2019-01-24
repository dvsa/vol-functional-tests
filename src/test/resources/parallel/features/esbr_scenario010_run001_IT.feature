# language: en

@SS
@ESBR
Feature: ESBR for English, Welsh and Scottish Areas

Scenario: ESBR in self-serve

Given I have a psv application with traffic area "H" and enforcement area "EA-H" which has been granted
When I upload an esbr file with "42" days notice
Then A short notice flag should not be displayed in selfserve

# Source feature: src/test/resources/features/SelfServe/esbr.feature
# Generated by Cucable

