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
| NIU Xijie    | <2121774069@qq.com>         | [BE-betterday](https://github.com/BE-betterday)          | NIU Xijie      |
| ZHANG Shuo   | <zhangshuo_se@163.com>      | [AnonTheChihaya](https://github.com/AnonTheChihaya)     | KatMelon       |
| HUANG Yichen | <15092713836@163.com>       | [AlicePotterr](https://github.com/AlicePotterr)          | HUANG Yichen   |
| LI Xinye     | <lxy15998403010@163.com>    | [Gin-lxy](https://github.com/Gin-lxy)               | LI Xinye       |
| LIN Junming  | <streamingbench@gmail.com>  | [streamingbenchvisual](https://github.com/streamingbenchvisual)  | LIN Junming    |
| MAO Xingzhi  | <raleighmxz@gmail.com>      | [SoapMacTavish21](https://github.com/SoapMacTavish21)       | MAO Xingzhi    |


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

| GitHub Account      | Real Name     | Tasks                                   |  
|---------------------|--------------|----------------------------------------|  
| **AnonTheChihaya**  | ZHANG Shuo    | User Data Model/DAO/Validation         |  
| **BE-betterday**    | NIU Xijie     | User-related Swing GUI, Account Filter/Search |  
| **SoapMacTavish21** | MAO Xingzhi   | Account Data Model/DAO/Validation       |  
| **AlicePotterr**    | HUANG Yichen  | Transaction Data Model/DAO/Validation  |  
| **Gin-lxy**         | LI Xinye      | Transaction-related Swing GUI          |  
| **streamingbenchvisual** | LIN Junming | Account-related Swing GUI              |  

### Sprint 2

**Completed User Stories:**  
✅ **US003** Dashboard Display (Assets/Monthly Trends)  
✅ **US005** Transaction Record Filtering  
✅ **US007** Preset Transaction Types  
✅ **US008** Automatic Transaction Recognition (Screenshot)  

**Assignments:**  

| GitHub Account      | Real Name     | Tasks                                   |  
|---------------------|--------------|----------------------------------------|  
| **AlicePotterr**    | HUANG Yichen  | Dashboard Implementation               |  
| **BE-betterday**    | NIU Xijie     | Transaction Filter/Search Function     |  
| **AnonTheChihaya**  | ZHANG Shuo    | Smart Transaction Auto-Input           |  
| **Gin-lxy**         | LI Xinye      | JUnit Tests for Config/DAOs            |  
| **SoapMacTavish21** | MAO Xingzhi   | JUnit Tests for Model/Util             |  
| **streamingbenchvisual** | LIN Junming | JUnit Tests for Service Validators   s  |

### Sprint 3

**Completed User Stories:**  
✅ **US009** Batch Transaction Import  
✅ **US010** Financial Planning  
✅ **US011** Savings Goal Tracking  
✅ **US012** Budget Plan Tracking  

**Assignments:**  

| GitHub Account      | Real Name     | Tasks                                                                 |  
|---------------------|--------------|----------------------------------------------------------------------|  
| **AnonTheChihaya**  | ZHANG Shuo    | Batch Transaction Import Implementation                             |  
| **AlicePotterr**    | HUANG Yichen  | Savings Goal Tracking Interface                                      |  
| **BE-betterday**    | NIU Xijie     | Savings Goal Data Model/DAO/Validation                               |  
| **streamingbenchvisual** | LIN Junming | Budget Plan Tracking Interface                                      |  
| **SoapMacTavish21** | MAO Xingzhi   | Budget Plan Data Model/DAO/Validation                               |  
| **Gin-lxy**         | LI Xinye      | JUnit Tests for Savings & Budget Modules                            |  

### Sprint 4  

**Completed User Stories:**  
✅ **US013** Recurring Payment Management  
✅ **US014** AI Financial Analysis Report  
✅ **US015** Abnormal Spending Alert  
✅ **US016** AI Assistant Support  

**Assignments:**  

| GitHub Account      | Real Name     | Tasks                                                                 |  
|---------------------|--------------|----------------------------------------------------------------------|  
| **AnonTheChihaya**  | ZHANG Shuo    | AI Assistant Interface & Miscellaneous Tasks                         |  
| **AlicePotterr**    | HUANG Yichen  | Recurring Payment Interface                                         |  
| **BE-betterday**    | NIU Xijie     | Recurring Payment Data Model/DAO/Validation                         |  
| **streamingbenchvisual** | LIN Junming | AI Analysis Report & Alert Business Logic + AI Prompts              |  
| **SoapMacTavish21** | MAO Xingzhi   | AI Analysis Report & Alert Interfaces                               |  
| **Gin-lxy**         | LI Xinye      | AI Assistant Business Logic + AI Prompts                            |  

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
