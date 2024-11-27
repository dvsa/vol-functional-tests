@messaging @ssMessageDisplay

Feature: External users can view the messaging tab, as well as create new messages, replies, message status, message notification count, and back button.

  @ss-messageheading-check
  Scenario Outline: Viewing messaging tab from an Under Consideration application
    Given as a "<user_type>" I have a "<Operator>" "<licence_type>" application
    And the caseworker completes and submits the application
    * i have logged in to self serve as "<user_type>"
    * i click the messages heading
    Then the messaging heading should be displayed
    Examples:
    | user_type   | Operator  | licence_type  |
    | consultant  | goods     | restricted    |
    | admin       | goods     | restricted    |

  @ss-message-new-conversation
  Scenario Outline: Starting new message conversation with the valid licence
    Given as a "<user_type>" I have a "<Operator>" "<licence_type>" application
    And the caseworker completes and submits the application
    * i have logged in to self serve as "<user_type>"
    * i click the messages heading
    * i click on start a new conversation link
    Examples:
      | user_type   | Operator  | licence_type  |
      | consultant  | goods     | restricted    |
      | admin       | goods     | restricted    |

  @ss-message-replyMessage
  Scenario: Operator reply for case worker message
    Given as a "<user_type>" I have a valid "<Operator>" "<licence_type>" licence
    And i create an admin and url search for my licence
    * i click the messages heading
    * i create a new conversation to operator
    * i have logged in to self serve as "<user_type>"
    * i redirect to the message tab to respond to the case worker's message
    Examples:
      | user_type   | Operator  | licence_type      |
      | consultant  | goods     | standard_national |
      | admin       | goods     | standard_national |

  @ss-message-openStatus-check
  Scenario: Check open message status
    Given as a "<user_type>" I have a valid "<Operator>" "<licence_type>" licence
    And i create an admin and url search for my licence
    * i click the messages heading
    * i create a new conversation to operator
    * i have logged in to self serve as "<user_type>"
    Then i view the new message that the caseworker has sent
    And i have opened a new message, which will appear as open
    Examples:
      | user_type   | Operator  | licence_type        |
      | consultant  | public     | special_restricted |
      | admin       | public     | special_restricted |

  @ss-message-count-check
  Scenario: Check notification count on external application
    Given as a "<user_type>" I have a valid "<Operator>" "<licence_type>" licence
    And i create an admin and url search for my licence
    * i click the messages heading
    * i create a new conversation to operator
    * i have logged in to self serve as "<user_type>"
    Then i validate the new message count appears on the messaging tab
    Examples:
      | user_type   | Operator  | licence_type |
      | consultant  | public    | restricted   |
      | admin       | public    | restricted   |

    @ss-message-back-to-conversation
    Scenario: Check back button on messaging page
      Given as a "<user_type>" I have a "<Operator>" "<licence_type>" application
      And the caseworker completes and submits the application
      * i have logged in to self serve as "<user_type>"
      * i click the messages heading
      * i click on start a new conversation link
      Then i click on back button to redirect to conversation page
      Examples:
        | user_type   | Operator  | licence_type            |
        | consultant  | public     | standard_international |
        | admin       | public     | standard_international |


