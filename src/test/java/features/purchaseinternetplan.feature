Feature: Internet Purchase Flow
  User should be able to purchase an internet plan successfully

  Scenario: User completes internet purchase successfully

    Given user is on the Internet Page
    When user selects the plan
    Then user enters zip code
    And user fills customer details
    When user selects product
    Then user accepts agreements
    And user proceeds to pricing
    And user proceeds to payment page
    And user enters payment details
    Then order should be submitted successfully
    And user return to homepage
