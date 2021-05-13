@Deprecated
Feature: Internal case worker annual bilateral application

  Background:
    Given I have a "goods" "standard_international" licence
      And I am on the VOL internal site
      And I am viewing a good operating licence on internal
      And the case worker has began an annual bilateral permit application

  @OLCS-23232
  Scenario: Date received is today's date and editable
    Then the date received should have today's date
    And  the total number of authorised vehicles should match what's on the licence
    When I save an application with an invalid date received
    Then the invalid date error message should be displayed
    When I have an application where the date received is in the future
    Then the error message for date received not being allowed to be a future date is displayed
    And  countries with an open window are displayed in alphabetical order
    And the year derived from the valid from date for each country with an open window is correct
    When number of annual bilateral permits is greater than the number of authorised vehicles
    And I save the annual bilateral permit on internal
    Then the error message for exceeding the maximum number of permits is displayed
    When the annual bilateral permit is cancelled
    Then there shouldn't be any ongoing applications

  @INTERNAL @OLCS-23232
  Scenario: Able to save application when number of permits is 0
    When I do not request any number of permits
    Then I'm able to save the annual bilateral application
    When I'm viewing my saved annual bilateral application
    Then I should save the cancel quick action
    But not have have the decisions submit button
    And I apply for one or more permits
    And I'm viewing my saved annual bilateral application
    When it has all sections completed
    Then there submit button is displayed

  @INTERNAL @OLCS-23232
  Scenario: Fee gets generated when one or more permits have been applied for
    When I apply for one or more permits
    Then a fee should be generated
    And check my current fees
    When I update the number of permits
    Then new fees are generated

  @OLCS-23235 @INTERNAL
  Scenario: Maximum number of permits that can be applied for takes into account live annual bilateral permits
    And a case worker has submitted an annual bilateral application
    When the case worker begins another annual bilateral permit application
    Then the maximum number of permits should account of the number of permits I've applied for in other permits