@lgv
@lgv-smoke
Feature: Interim licences for standard international operators with LGVs

  Scenario Outline: Interim Vehicle Authority greater than Application Vehicle Authority for standard international operators with LGVs
    Given i have a valid "goods" "<licenceType>" licence
    And i create and submit an operating centre variation with "<HGVs>" hgvs and "<LGVs>" lgvs
    And i create an admin and url search for my variation
    When I have an interim vehicle authority with "<intHGVs>" hgvs and "<intLGVs>" lgvs
    Then A "<errorType>" error appears when i save the interim licence

    Examples:
      | licenceType            | HGVs | LGVs | intHGVs | intLGVs| errorType |
      | standard_international | 5    | 5    | 6       | 0      | HGV       |
      | standard_international | 5    | 5    | 0       | 6      | LGV       |

  Scenario Outline: Interim Vehicle Authority equals the Application Vehicle Authority for standard international operators with LGVs
    Given i have a valid "goods" "<licenceType>" licence
    And i create and submit an operating centre variation with "<HGVs>" hgvs and "<LGVs>" lgvs
    And i create an admin and url search for my variation
    When I have an interim vehicle authority with "<intHGVs>" hgvs and "<intLGVs>" lgvs
    Then I should be able to save the application without any errors

    Examples:
      | licenceType            | HGVs | LGVs | intHGVs | intLGVs|
      | standard_international | 5    | 5    | 5       | 0      |
      | standard_international | 5    | 5    | 0       | 5      |
      | standard_international | 5    | 5    | 5       | 5      |

  Scenario Outline: Interim Vehicle Authority less than Application Vehicle Authority for standard international operators with LGVs
    Given i have a valid "goods" "<licenceType>" licence
    And i create and submit an operating centre variation with "<HGVs>" hgvs and "<LGVs>" lgvs
    And i create an admin and url search for my variation
    When I have an interim vehicle authority with "<intHGVs>" hgvs and "<intLGVs>" lgvs
    Then I should be able to save the application without any errors

    Examples:
      | licenceType            | HGVs | LGVs | intHGVs | intLGVs|
      | standard_international | 5    | 5    | 4       | 0      |
      | standard_international | 5    | 5    | 0       | 4      |
      | standard_international | 5    | 5    | 4       | 4      |
