Feature: Various accessibility Rules Checks

  Scenario: Keyboard - Reach all links (text or image), form controls and page functions with keyboard
    Given i have a valid "goods" "sn" licence
    When i am on the vehicle details page
    Then i should be able to navigate page using my keyboard

  Scenario: Keyboard - Skip to main content on all VOL pages
    Given i have a valid "goods" "sn" licence
    When i navigate to self serve licence main pages and skip to main content
    Then i should be on the main content of the page