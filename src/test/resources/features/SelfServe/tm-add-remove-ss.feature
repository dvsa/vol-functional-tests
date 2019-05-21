@SS
@SS-LAST-TM-TRIGGER
@OLCS-19479
@ss_regression

Feature: Set and check criteria for triggering automatic letter

  Background:
    Given i have a valid "public" "si" licence

  Scenario: Generate letter for valid licence when ss removes last TM
    When a self-serve user removes the last TM
    Then a flag should be set in the DB

  Scenario: No letter generated when ss adds a TM
    When a self-serve user adds a TM
