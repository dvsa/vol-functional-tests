@WebDav
Feature: Check that the OS Version is displayed in the user account details

  Scenario Outline: Check that OS Version in returned null
    Given i have registered a new "<user_role>" user
    When i view their user details
    Then the OS Type value should be null

    Examples:
      | user_role                  |
      | internal-admin             |
      | internal-limited-read-only |
      | internal-read-only         |
      | internal-case-worker       |


  Scenario Outline: Check that 'limited-read-only', 'read-only' and 'case-worker' users cannot update their OS Version
    Given i have registered a new "<user_role>" user
    And i view their user details
    When they attempt to update their OS version to "<os_version>"
    Then the OS Type value should be null

    Examples:
      | user_role                  | os_version |
      | internal-limited-read-only | windows_7  |
      | internal-read-only         | windows_7  |
      | internal-case-worker       | windows_7  |
      | internal-limited-read-only | windows_10 |
      | internal-read-only         | windows_10 |
      | internal-case-worker       | windows_10 |

  Scenario Outline: Check that an Internal-Admin user can update 'limited-read-only', 'read-only' and 'case-worker' users OS Type to Windows 7 and 10
    Given i have registered a new "<user_role>" user
    When i attempt to update their OS version to "<os_version>"
    And i view their user details
    Then their OS Type value should be displaying "<os_version>"

    Examples:
      | user_role                  | os_version |
      | internal-limited-read-only | windows_7  |
      | internal-read-only         | windows_7  |
      | internal-case-worker       | windows_7  |
      | internal-admin             | windows_7  |
      | internal-admin             | windows_10 |
      | internal-limited-read-only | windows_10 |
      | internal-read-only         | windows_10 |
      | internal-case-worker       | windows_10 |

  Scenario Outline: Check that an Internal-Admin user can update 'limited-read-only', 'read-only' and 'case-worker' users OS Type multiple times
    Given i have registered a new "<user_role>" user
    When i attempt to update their OS version to "<os_version>"
    And i view their user details
    Then their OS Type value should be displaying "<os_version>"
    And i attempt to update their OS version to "<new_os_version>"
    Then their new OS Type should be "<new_os_version>"

    Examples:
      | user_role                  | os_version | new_os_version |
      | internal-limited-read-only | windows_7  | windows_10     |
      | internal-read-only         | windows_7  | windows_10     |
      | internal-case-worker       | windows_7  | windows_10     |
      | internal-admin             | windows_7  | windows_10     |
      | internal-admin             | windows_10 | windows_7      |
      | internal-limited-read-only | windows_10 | windows_7      |
      | internal-read-only         | windows_10 | windows_7      |
      | internal-case-worker       | windows_10 | windows_7      |