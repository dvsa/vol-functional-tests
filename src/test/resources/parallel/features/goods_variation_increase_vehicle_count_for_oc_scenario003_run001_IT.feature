# language: en

@SS
@SS-GOODS-VAR-OC-INCREASE-VEHICLE
@OLCS-21133
Feature: Goods Variation increase vehicle count for an OC

Scenario: Increasing the vehicle count to an invalid character for authorised vehicles

Given i have a valid "goods" licence
When A selfserve user increases the vehicle authority by invalid charecters
Then An error should appear

# Source feature: src/test/resources/features/SelfServe/goods-variation-increase-vehicle-count-for-oc.feature
# Generated by Cucable

