@lgv
@cross-browser

Feature: 3 - Publications generate on LGV related changes and regression

  Scenario Outline: The out of objection date is populated 22 days after the publication date
    Given i have a valid "goods" "<licenceType>" licence
    When i create and submit an operating centre variation with "<HGVs>" hgvs and "<LGVs>" lgvs
    Then the out of objection date is present on the application 22 days after the publication date

    Examples:
      | licenceType            | HGVs | LGVs |
      | standard_international | 5    | 5    |
      | standard_international | 8    | 0    |
      | standard_national      | 8    | 0    |
