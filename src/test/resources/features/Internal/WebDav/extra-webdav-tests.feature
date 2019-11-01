@INT
@WebDav
Feature: All WebDav functionality tests

  Background:
    Given i have a valid "goods" "sn" licence
    And i have logged in to internal

  Scenario Outline: I update my operating system settings
    When i update my operating system on internal to "<OsType>"
    Then the operating system should be updated to "<OsType>"

    Examples:
    | OsType     |
    | Windows 10 |
    | Windows 7  |