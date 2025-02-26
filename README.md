# BBC API Test Project

This project is a set of automated tests written in Scala to verify the functionality and performance of the BBC API endpoint (specifically `/api/RMSTest/ibltest`). It uses Cucumber for behavior-driven development (BDD) and ScalaTest for assertions.

## Purpose

The tests aim to validate:

*   API response status codes.
*   API response times.
*   Data integrity of the schedule items returned by the API.
*   Specific business rules related to the schedule data .
*   HTTP Header validation

## Table of Contents

1.  [Prerequisites]
2.  [Getting Started]
3.  [Running the Tests]
4.  [Project Structure]
5.  [Code Overview]
6.  [Dependencies]
7.  [Contributing]

## Prerequisites

*   **Java Development Kit (JDK):**  You need a JDK installed (version 8 or higher is recommended).  [Download JDK](https://www.oracle.com/java/technologies/javase-downloads.html)
*   **Scala Build Tool (SBT):** SBT is used to build and run the project.  Install it from [https://www.scala-sbt.org/](https://www.scala-sbt.org/).
*   **Git:**  Required to clone the repository. [Download Git](https://git-scm.com/downloads)

## Getting Started

1.  **Clone the Repository:**

    ```bash
    git clone <https://github.com/Anjana00703/bbc_api_test.git>  
    cd bbc_api_test 
    ```

2.  **Verify SBT Installation:**

    ```bash
    sbt sbtVersion
    ```
    This command should output the SBT version.

## Running the Tests

The tests are written using Cucumber and ScalaTest. To run them:

```bash

## Project Sturcture
sbt test
bbc_api_test/
├── src/
│   ├── main/
│   │   └── scala/
│   │       ├── ApiClient.scala     # API client logic (requests, data parsing)
│   └── test/
│       ├── features/
│       │       └── api_tests.feature # Cucumber feature file (scenarios)
│       └── scala/
│           └── definitions
|              └── StepDefinitions.scala # Cucumber step definitions (Scala code)     
├── build.sbt             # SBT build definition
├── .gitignore            # Specifies intentionally untracked files that Git should ignore
├── README.md             # This file


## Code Overview

1.  ** src/main/scala/ApiClient.scala: **
'Defines the ApiClient object, which handles making requests to the BBC API.
'Includes functions to get schedule data, parse JSON responses into Scala objects, and define the data models (ScheduleItem, Episode).
'Uses the requests library for making HTTP requests and upickle for JSON serialization/deserialization.
'The getSchedule() functions take an optional date parameter to filter the API response.

2.** src/test/scala/definition/StepDefinitions.scala:**
'Contains the Cucumber step definitions, which link the steps in the feature files to the Scala code that executes the tests.
'Uses requests library for making HTTP requests, and org.scalatest.matchers.must.Matchers for assertions.
'Includes steps to verify status codes, response times, data integrity, and business rules.
'Uses pattern matching to handle different fields in the Then steps.
'The parseSchedule function handles the parsing of the JSON response into ScheduleItem objects using upickle.

3.** src/test/resources/features/api_tests.feature:**
'The Cucumber feature file that defines the test scenarios in a human-readable format using Gherkin syntax.
'Outlines the different test cases, such as checking the status code, response time, and data validation.

4. **build.sbt:**
'The SBT build definition file that specifies the project's dependencies, Scala version, and other build settings.
'Includes the necessary dependencies for requests, scalatest, cucumber-scala, cucumber-junit, and upickle.

## Dependencies
'The project uses the following dependencies, managed by SBT:
'com.lihaoyi::requests:0.8.0: For making HTTP requests.
'org.scalatest::scalatest:3.2.12: For writing and running tests.
'io.cucumber::cucumber-scala:8.14.0: For integrating Cucumber with Scala.
'io.cucumber:cucumber-junit:7.14.0: For JUnit reporting of Cucumber tests.
'com.lihaoyi::upickle:3.1.3: For JSON serialization/deserialization.