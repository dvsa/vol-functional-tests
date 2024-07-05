@continuation_reminder

Feature: Continuation reminder is sent to operator

  Background:
    Given i have a valid "goods" "standard_national" licence
    When i change my continuation and review date on Internal
    And i generate a continuation

  Scenario: Start Digital Continuation
    And the operator partially completes a continuation in self serve
    And i trigger the digital-continuation-reminders batch job

