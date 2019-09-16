@SS-changes-not-variation
@ss_regression

Feature: Self Serve users can make changes to their valid licence.

  Background:
    Given i have a valid "public" "sn" licence
    And i have logged in to self serve

  Scenario: A self serve user changes their business details
    When i make changes to the business details page
    Then the changes to the business details page are made

  Scenario: A self serve user changes their addresses
    When i make changes to the addresses page
    Then the changes to the addresses page are made

  Scenario: A self serve user changes their vehicles
    When i make changes to the vehicles page
    Then the changes to the vehicles page are made

  Scenario: A self serve user changes their licence discs (public only)
    When i make changes to the licence discs page
    Then the changes to the licence discs page are made

  Scenario: A self serve user changes their safety and compliance
    When i make changes to the safety and compliance page
    Then the changes to the safety and compliance page are made

# mvn clean verify -Denv=demo -Dbrowser=chrome -DdbUsername=olcsapi -DdbPassword=NsbJBrYaYAwu0Ysm -Dcucumber.options="--tags @SS-changes-not-variation,@SS-CHECK-DOCUMENTS,@OLCS-21298,@SS-ADD-DIRECTOR,@ESBR,@SS-EXTERNAL-SEARCH,@External-Search-Filter,@OLCS-22913,@OLCS-23724,@OLCS-24464,@ss-pay-app,@OLCS-22275"
# mvn clean verify -Denv=da -Dbrowser=chrome -DdbUsername=olcsapi -DdbPassword=NsbJBrYaYAwu0Ysm -Dcucumber.options="--tags @SS-changes-not-variation,@SS-CHECK-DOCUMENTS,@OLCS-21298,@SS-ADD-DIRECTOR,@ESBR,@SS-EXTERNAL-SEARCH,@External-Search-Filter,@OLCS-22913,@OLCS-23724,@OLCS-24464,@ss-pay-app,@OLCS-22275"