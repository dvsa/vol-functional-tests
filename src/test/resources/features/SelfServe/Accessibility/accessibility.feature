@SS
Feature: Various accessibility Rules Checks

  Scenario: Keyboard - Reach all links (text or image), form controls and page functions with keyboard
    Given i have a valid "goods" "standard_national" licence
    When i am on the vehicle details page
    Then i should be able to navigate page using my keyboard

  Scenario: Keyboard - Skip to main content on all VOL application pages
    Given i have a "public" "standard_national" "GB" application in traffic area
      | b |
    When i navigate to self serve application main pages i can skip to main content

  Scenario: Keyboard - Skip to main content on all VOL licence pages
    Given i have a valid "goods" "standard_national" licence
    When i navigate to self serve licence main pages i can skip to main content

  Scenario: Keyboard - Skip to main content on all VOL nav bar pages
    Given i have a valid "goods" "standard_national" licence
    When i navigate to self serve licence nav bar pages i can skip to main content

  Scenario: Keyboard - Skip to main content on all VOL surrender pages
    Given i have a valid "goods" "standard_national" licence
    When i navigate to self serve licence surrender pages i can skip to main content