@lgv
Feature: Interim licences generate on LGV related changes and regression


  Scenario Outline: Interim licences generate with lgvs
    Given i have a valid "goods" "<licenceType>" licence
    And i create and submit an operating centre variation with "<HGVs>" hgvs and "<LGVs>" lgvs
    And i create an admin and url search for my variation
    When I have an interim vehicle authority greater than my application vehicle authority
    #Then I should get a "<errorType>" error when i save the application
    Then A "<errorType>" error appears when i save the interim licence

    Examples:
      | licenceType            | HGVs | LGVs | intHGVs | intLGVs| errorType |
      | standard_international | 5    | 5    | 6       | 0      | HGV       |
      #| standard_international | 5    | 5    | 6       | 0      | HGV       |
      #| standard_international | 8    | 0    | HGV           |
      #| standard_international | 8    | 5    | HGV and LGV   |
      #| standard_national      | 8    | 0    | HGV           |