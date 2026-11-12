# @ss_regression
@vol763 @footer-policy-links
Feature: Self Serve footer policy links

  Scenario Outline: Follow a policy link from the sign in page
    Given I open the Self Serve sign in page
    When I follow the footer link to "<path>"
    Then the destination page contains "<content>"

    Examples:
      | path                     | content                                                      |
      | /cookies/settings        | Cookies are files saved on your phone, tablet or computer    |
      | /privacy-notice          | Traffic Commissioners for Great Britain: privacy notice      |
      | /accessibility-statement | How you should be able to use this website                   |
