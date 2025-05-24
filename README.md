<!-- Improved compatibility of back to top link: See: https://github.com/othneildrew/Best-README-Template/pull/73 -->
<a id="readme-top"></a>
<!--
*** Thanks for checking out the Best-README-Template. If you have a suggestion
*** that would make this better, please fork the repo and create a pull request
*** or simply open an issue with the tag "enhancement".
*** Don't forget to give the project a star!
*** Thanks again! Now go create something AMAZING! :D
-->

<!-- PROJECT LOGO -->
<br />
<div align="center">
  <a href="https://github.com/AnonTheChihaya/financial-tracker">
    <img src="images/logo.png" alt="Logo" width="80" height="80">
  </a>

  <h3 align="center">Financial Tracker</h3>

  <p align="center">
     EBU6304 Software Engineering - Group50 Project Repo
    <br />
    <br />
    <a href="https://github.com/AnonTheChihaya/financial-tracker/releases">Latest Release</a>
    ·
    <a href="https://github.com/AnonTheChihaya/financial-tracker/issues">Report Bug</a>
  </p>
</div>

## About The Project

This is the repository for module EBU6304 Software Engineering coursework.

Hello from Group 50!

## Contributors

| Name         | Email                     | GitHub Account        | Git Identity   |
|--------------|---------------------------|-----------------------|----------------|
| NIU Xijie    | <2121774069@qq.com>         | BE-betterday          | NIU Xijie      |
| ZHANG Shuo   | <zhangshuo_se@163.com>      | [AnonTheChihaya](https://github.com/AnonTheChihaya)     | KatMelon       |
| HUANG Yichen | <15092713836@163.com>       | AlicePotterr          | HUANG Yichen   |
| LI Xinye     | <lxy15998403010@163.com>    | Gin-lxy               | LI Xinye       |
| LIN Junming  | <streamingbench@gmail.com>  | streamingbenchvisual  | LIN Junming    |
| MAO Xingzhi  | <raleighmxz@gmail.com>      | SoapMacTavish21       | MAO Xingzhi    |


<a href="https://github.com/AnonTheChihaya/financial-tracker/graphs/contributors">
  <img src="https://contrib.rocks/image?repo=AnonTheChihaya/financial-tracker" alt="contrib.rocks image" />
</a>

## Roadmap & Assignment

### Sprint 1

**Completed User Stories:**  
✅ **US001** User Registration and Login (TOTP 2FA)  
✅ **US002** User Information Modification  
✅ **US004** Account Management (Savings/Investment)  
✅ **US006** Manual Transaction Recording  

**Assignments:**  

| GitHub Account      | Real Name     | Tasks                                                                 | GitHub PRs               |  
|---------------------|--------------|----------------------------------------------------------------------|--------------------------|  
| **AnonTheChihaya**  | ZHANG Shuo    | User Data Model/DAO/Validation (#1)                                  | #1 (Merged)              |  
| **BE-betterday**    | NIU Xijie     | User-related Swing GUI (#2), Account Filter/Search (#10)             | #2, #10 (Merged)         |  
| **SoapMacTavish21** | MAO Xingzhi   | Account Data Model/DAO/Validation (#3)                                | #3, #13 (Merged)         |  
| **AlicePotterr**    | HUANG Yichen  | Transaction Data Model/DAO/Validation (#5)                           | #5, #9 (Merged)          |  
| **Gin-lxy**         | LI Xinye      | Transaction-related Swing GUI (#6)                                  | #6, #12 (Merged)         |  
| **streamingbenchvisual** | LIN Junming | Account-related Swing GUI (#4)                                | #4 (Merged)              |  

### Sprint 2

**Completed User Stories:**  
✅ **US003** Dashboard Display (Assets/Monthly Trends)  
✅ **US005** Transaction Record Filtering  
✅ **US007** Preset Transaction Types  
✅ **US008** Automatic Transaction Recognition (Screenshot)  

**Assignments:**  

| GitHub Account      | Real Name     | Tasks                                                                 | GitHub PRs               |  
|---------------------|--------------|----------------------------------------------------------------------|--------------------------|  
| **AlicePotterr**    | HUANG Yichen  | Dashboard Implementation (#9)                                        | #9 (Merged)              |  
| **BE-betterday**    | NIU Xijie     | Transaction Filter/Search Function (#10)                             | #10 (Merged)             |  
| **AnonTheChihaya**  | ZHANG Shuo    | Smart Transaction Auto-Input (#11)                                   | #11 (Merged)             |  
| **Gin-lxy**         | LI Xinye      | JUnit Tests for Config/DAOs (#12)                                    | #12 (Merged)             |  
| **SoapMacTavish21** | MAO Xingzhi   | JUnit Tests for Model/Util (#13)                                     | #13 (Merged)             |  
| **streamingbenchvisual** | LIN Junming | JUnit Tests for Service Validators (#14)                      | #14 (Merged)             |  

### Sprint 3&4

Coming soon...

## Quick Start

### Option 1: Run from Release JAR

1. Download the latest release JAR file (`FinancialTracker-v0.2.0-sprint2-jar-with-dependencies.jar`) from the Releases section
2. Ensure you have Java 21 or later installed on your system
3. Run the application using the following command:

   ```bash
   java -jar FinancialTracker-v0.2.0-sprint2-jar-with-dependencies.jar
   ```

### Option 2: Build and Run from Source

#### Prerequisites

- Java 21 JDK installed
- Maven 3.6.0 or later installed

#### Steps

1. Clone the repository:

   ```bash
   git clone https://github.com/AnonTheChihaya/financial-tracker.git
   cd FinancialTracker
   ```

2. Build the project with all dependencies:

   ```bash
   mvn clean package
   ```

3. Run the application:

   ```bash
   java -jar target/FinancialTracker-1.0-SNAPSHOT-jar-with-dependencies.jar
   ```

#### More Development Commands

To compile and run tests:

  ```bash
  mvn test
  ```

To create a new executable JAR with dependencies:

  ```bash
  mvn assembly:single
  ```

To clean build artifacts:

  ```bash
  mvn clean
  ```

The built JAR file will include all dependencies and be ready for distribution.

Note: The application requires Java 21 to run. Make sure your environment meets this requirement before proceeding.

<!-- LICENSE -->
## License

Copyright © 2025 EBU6304_Group50. All rights reserved.

This work is provided solely for educational use by authorized instructors and teaching staff of QMUL & BUPT. All other uses are strictly prohibited without prior written permission.

Violations will be reported as academic dishonesty and may result in disciplinary action.
