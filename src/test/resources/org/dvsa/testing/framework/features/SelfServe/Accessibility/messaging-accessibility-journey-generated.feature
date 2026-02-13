@accessibility
@messaging-accessibility
Feature: Check that self-serve messaging journey is accessible

  @ss-message-dashboard-accessibility
  Scenario Outline: Accessibility scan for messaging dashboard
    Given as a "<user_type>" I have a valid "<Operator>" "<licence_type>" licence
    And i have logged in to self serve as "<user_type>"
    When i click the messages heading
    And i scan for accessibility violations
    Then no issues should be present on the page
    Examples:
      | user_type   | Operator  | licence_type  |
      | consultant  | goods     | restricted    |
      | admin       | goods     | restricted    |

  @ss-message-new-conversation-accessibility
  Scenario Outline: Accessibility scan for new conversation creation
    Given as a "<user_type>" I have a valid "<Operator>" "<licence_type>" licence
    And i have logged in to self serve as "<user_type>"
    When i click the messages heading
    And i click on start a new conversation link and select the licence number
    And i scan for accessibility violations
    Then no issues should be present on the page
    Examples:
      | user_type   | Operator  | licence_type  |
      | consultant  | goods     | restricted    |
      | admin       | goods     | restricted    |

  @ss-message-conversation-view-accessibility
  Scenario Outline: Accessibility scan for conversation view with open message
    Given as a "<user_type>" I have a valid "<Operator>" "<licence_type>" licence
    And i create an admin and url search for my licence
    And i have logged in to self serve as "<user_type>"
    When i click the messages heading
    And i click on start a new conversation link and select the licence number
    And i have opened a new message, which will appear as open
    And i scan for accessibility violations
    Then no issues should be present on the page
    Examples:
      | user_type   | Operator  | licence_type  |
      | consultant  | goods     | restricted    |
      | admin       | goods     | restricted    |

  @ss-message-back-navigation-accessibility
  Scenario Outline: Accessibility scan for back to conversations navigation
    Given as a "<user_type>" I have a valid "<Operator>" "<licence_type>" licence
    And i create an admin and url search for my licence
    And i have logged in to self serve as "<user_type>"
    When i click the messages heading
    And i click on start a new conversation link and select the licence number
    And i click on back button to redirect to conversation page
    And i scan for accessibility violations
    Then no issues should be present on the page
    Examples:
      | user_type   | Operator  | licence_type  |
      | consultant  | goods     | restricted    |
      | admin       | goods     | restricted    |

  @ss-message-reply-accessibility
  Scenario Outline: Accessibility scan for message reply functionality
    Given as a "<user_type>" I have a valid "<Operator>" "<licence_type>" licence
    And i create an admin and url search for my licence
    When i click the messages heading
    And i create a new conversation to operator
    And i have logged in to self serve as "<user_type>"
    And i redirect to the message tab to respond to the case worker's message
    And i scan for accessibility violations
    Then no issues should be present on the page
    Examples:
      | user_type   | Operator  | licence_type      |
      | consultant  | goods     | standard_national |
      | admin       | goods     | standard_national |

  @ss-message-notification-count-accessibility
  Scenario Outline: Accessibility scan for message notification display
    Given as a "<user_type>" I have a valid "<Operator>" "<licence_type>" licence
    And i create an admin and url search for my licence
    When i click the messages heading
    And i create a new conversation to operator
    And i have logged in to self serve as "<user_type>"
    And i validate the new message count appears on the messaging tab
    And i scan for accessibility violations
    Then no issues should be present on the page
    Examples:
      | user_type   | Operator  | licence_type      |
      | consultant  | goods     | standard_national |
      | admin       | goods     | standard_national |

  @ss-message-error-handling-accessibility
  Scenario Outline: Accessibility scan for message error states
    Given as a "<user_type>" I have a valid "<Operator>" "<licence_type>" licence
    And i have logged in to self serve as "<user_type>"
    When i click the messages heading
    And i send a new message without selecting a category, licence or application number and text
    And i should get an error message
    And i scan for accessibility violations
    Then no issues should be present on the page
    Examples:
      | user_type   | Operator  | licence_type  |
      | consultant  | goods     | restricted    |
      | admin       | goods     | restricted    |

  @ss-message-reply-error-accessibility
  Scenario Outline: Accessibility scan for reply error states
    Given as a "<user_type>" I have a valid "<Operator>" "<licence_type>" licence
    And i create an admin and url search for my licence
    And i have logged in to self serve as "<user_type>"
    When i send a reply without entering a message in the text field, and an error message will appear
    And i scan for accessibility violations
    Then no issues should be present on the page
    Examples:
      | user_type   | Operator  | licence_type      |
      | consultant  | goods     | standard_national |
      | admin       | goods     | standard_national |

  @ss-message-tab-visibility-accessibility
  Scenario Outline: Accessibility scan for message tab visibility states
    Given as a "<user_type>" I have a valid "<Operator>" "<licence_type>" licence
    And i have logged in to self serve as "<user_type>"
    When the messages tab is displayed on the dashboard
    And i scan for accessibility violations
    Then no issues should be present on the page
    Examples:
      | user_type   | Operator  | licence_type  |
      | consultant  | goods     | restricted    |
      | admin       | goods     | restricted    |

  @ss-message-complete-journey-accessibility
  Scenario Outline: End-to-end accessibility scan for complete messaging journey
    Given as a "<user_type>" I have a valid "<Operator>" "<licence_type>" licence
    And i create an admin and url search for my licence
    And i have logged in to self serve as "<user_type>"
    When i click the messages heading
    And i click on start a new conversation link and select the licence number
    And i click Send message to send a message to the caseworker
    And i click on back button to redirect to conversation page
    And i have opened a new message, which will appear as open
    And i scan for accessibility violations
    Then no issues should be present on the page
    Examples:
      | user_type   | Operator  | licence_type  |
      | consultant  | goods     | restricted    |
      | admin       | goods     | restricted    |