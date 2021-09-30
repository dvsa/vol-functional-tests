@lgv
Feature: Interim licences generate on LGV related changes and regression


  Scenario Outline: Interim licences generate with lgvs
    Given i have a valid "goods" "<licenceType>" licence
    And i create and submit an operating centre variation with "<HGVs>" hgvs and "<LGVs>" lgvs
    #And i create and submit and grant an operating centre variation with "<HGVs>" hgvs and "<LGVs>" lgvs
    #And i create an admin and url search for my licence
    And i create an admin and url search for my variation
    #And i create a variation in internal
    When I have an interim vehicle authority greater than my application vehicle authority
    Then I should get a "<errorType>" error when i save the application
    #And i create and submit an operating centre variation with "<HGVs>" hgvs and "<LGVs>" lgvs
    #When the corresponding publication is generated and published
    #Then the publication is visible via self serve search
    #And the "New Variation" "<variationType>" publication text is correct with "<HGVs>" hgvs and "<LGVs>" lgvs

    Examples:
      | licenceType            | HGVs | LGVs | intHGVs | intLGVs| errorType |
      | standard_international | 5    | 5    | 6       | 0      | HGV       |
      #| standard_international | 5    | 5    | 6       | 0      | HGV       |
      #| standard_international | 8    | 0    | HGV           |
      #| standard_international | 8    | 5    | HGV and LGV   |
      #| standard_national      | 8    | 0    | HGV           |