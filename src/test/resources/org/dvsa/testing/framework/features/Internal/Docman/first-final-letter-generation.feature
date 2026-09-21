@int-regression
@letter-generation
@wip

Feature: First and final letter generation end to end journeys
  As a caseworker
  I want to generate first and final request letters against an application under consideration
  So that the correct wording, selected issues, appendices and operator details reach the operator


  Background:
    Given I have a "goods" "standard_national" application which is under consideration
    And i create an admin and url search for my application
    And I navigate directly to my application in internal
    And i view the Docs & attachments page
    And the licence details panel should show the operator name and licence number
    And i open the Create letter modal for the first and final request template

  @wording @first-final
  Scenario: First and final request letters generate different wording
    When i select the "Adverts" issue "Date of publication is missing"
    And i select the "First request" option
    And i create the letter
    Then the letter wording is captured for "First request"
    When i open the Create letter modal for the first and final request template
    And i select the "Adverts" issue "Date of publication is missing"
    And i select the "Final request" option
    And i create the letter
    Then the letter wording is captured for "Final request"
    And the final request wording should differ from the first request wording

  @edited-sections
  Scenario Outline: Selected issues are shown in the generated letter
    When i select the "<section>" issue "<issue>"
    And i select the "<choice>" option
    And i create the letter
    Then the letter should contain the "<issue>" issue wording
    And the letter should not contain the "<unselectedIssue>" issue wording

    Examples:
      | section | issue                           | unselectedIssue            | choice        |
      | Adverts | Date of publication is missing  | Unsuitable type of publication | First request |
      | Adverts | Advert evidence is missing      | Unsuitable type of publication | Final request |

  @mandatory-sections @validation
  Scenario: Create letter is blocked until an issue or appendix is chosen
    Then the Create letter button should be disabled
    When i select the "First request" option
    And i attempt to create the letter
    Then the content options warning should be displayed

  @mandatory-sections @validation
  Scenario: A warning is displayed when the mandatory first or final request choice is missing
    When i select the "Adverts" issue "Date of publication is missing"
    And i attempt to create the letter
    Then the first or final request warning should be displayed
    When i select the "First request" option
    And i create the letter
    Then the letter preview should show the category, subcategory and template

  @appendices
  Scenario Outline: Appendices are linked to the generated letter
    When i select the "Adverts" issue "Date of publication is missing"
    And i select the "<appendix>" appendix
    And i select the "<choice>" option
    And i create the letter
    Then the letter should contain the "<appendix>" appendix
    And the appendix link should open the correct document

    Examples:
      | appendix                          | choice        |
      | Statutory declaration             | First request |
      | New application advert template   | Final request |

  @operator-details
  Scenario Outline: Operator and application information is correctly populated
    When i select the "Adverts" issue "Date of publication is missing"
    And i select the "<choice>" option
    And i create the letter
    Then the letter preview should show the category, subcategory and template
    And the letter should display the operator name and licence number
    And the letter should display the application reference

    Examples:
      | choice        |
      | First request |
      | Final request |

  @e2e
  Scenario: End to end first then final request letter journey
    When i select the "Adverts" issue "Date of publication is missing"
    And i select the "Statutory declaration" appendix
    And i select the "First request" option
    And i create the letter
    Then the letter wording is captured for "First request"
    And the letter should display the operator name and licence number
    And the letter should contain the "Statutory declaration" appendix
    When i prepare the letter to be sent
    Then the letter should be listed in Docs & attachments
    When i open the Create letter modal for the first and final request template
    And i select the "Adverts" issue "Date of publication is missing"
    And i select the "Final request" option
    And i create the letter
    Then the letter wording is captured for "Final request"
    And the final request wording should differ from the first request wording
    When i prepare the letter to be sent
    Then the letter should be listed in Docs & attachments
