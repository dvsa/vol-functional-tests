@SS
@OLCS-22256

  Feature: Operator declares Standard National licence details

    Scenario: Licence in operators possession
      Given the user selects In your possession option
      Then they should be presented with the text You must destroy your operator licence

      Scenario: Licence is lost
        Given the user select the Lost option
        When they are presented with the text box – as per the screen shot
        Then they should provide additional information

    Scenario: Licence is stolen
      Given the user select the Stolen option
      When they are presented with the text box – as per the screen shot
      Then they should provide additional information and a crime reference number

    Scenario: Surrender licence
      Given the licence is a standard national
      When I click on Save and continue
      Then I should be taken to the Review your discs and documentations page