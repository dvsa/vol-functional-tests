# @ss_regression
@vol763
Feature: Operators can review and switch between current licences

  @multi-licence-dashboard
  Scenario: Current licence summaries match and each licence opens from the dashboard
    Given I have "2" "goods" "standard_national" licences
    And i have a self serve account
    Then the current licence summaries should match and each licence should open from the dashboard
      | status | licence type     | area                  |
      | Valid  | Standard National | North East of England |
      | Valid  | Standard National | North West of England |

  @user-multiple-licence-access
  Scenario: Added organisation user can access each of its current licences
    Given I have "2" "goods" "standard_national" licences
    And i have a self serve account
    And i navigate to the manage users page
    When i add a user
    And I sign in as the added user
    Then the current licence summaries should match and each licence should open from the dashboard
      | status | licence type     | area                  |
      | Valid  | Standard National | North East of England |
      | Valid  | Standard National | North West of England |
