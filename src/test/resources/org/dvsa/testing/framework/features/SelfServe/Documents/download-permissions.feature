@downloadPermissions

Feature: Permissions for Self Service users downloading documents

Background:
  Given i have a valid "goods" "standard_national" licence
  And i create an admin and url search for my licence

  Scenario: Operator is able to download the document
    And i print a licence document
    And i note the document id
    And i log back in as the operator
    Then i should be able to download the file

  Scenario: Operator is prevented from downloading the document
    And i change the operator correspondence to Post
    And i print a licence document
    And i note the document id
    And i log back in as the operator
    Then i should not be able to download the file