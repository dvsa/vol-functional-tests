Feature: Operator can use Self Service links

@tops-report @ss_regression
    Scenario: Operator can navigate to DVSA Operator Reports from nonprod
      Given I have a "Goods" "Restricted" licence
      When I note the operator name on the Dashboard page
      And I click the DVSA Operator Reports link
      Then I am taken to the redirect page for TOPS

#  #@PP-SMOKE
#    Scenario: Operator can navigate to DVSA Operator Reports from prep
#      Given I log into prep "self serve" account with user "prepUser"
#      When I note the operator name on the Dashboard page
#      And I click the DVSA Operator Reports link
#      Then the operator name should be displayed on the Operator Reports Service page
