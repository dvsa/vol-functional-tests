@INT
@PSV-GENERATE-TM-LETTER
@-OLCS-19479
Feature: Set and check criteria for triggering automatic letter

  Background:
    Given i have a valid "public" licence

  Scenario: Generate letter for valid licence
    When the transport manager has been removed by an internal user
    Then a flag should be set in the DB

#  Scenario: Generate letter for suspended licence
#    Given the licence status is "suspend"
#    When the transport manager has been removed by an internal user
#    Then a flag should be set in the DB
#
#  Scenario:
#    Given the licence status is "curtail"
#    When the transport manager has been removed by an internal user
#    Then a flag should be set in the DB