# Mend.io Java Development Task

## Overview

This is a repo with a Java Development Task assignment solution.

## Features

- Parallelized sanity tests of GitHub.com 4 MVP capabilities
    - Login
    - Create a new repository
    - Delete a repository
    - Get billing information
- Each step is first done by UI automation, then performance is optimized through API requests.
- Properties and Parameters are read from (in order):
    - `.xml` files in `src/test/resources`
    - System Properties (best for parallelized execution)
    - `config.properties` in `src/main/resources`
        - Default values
- Supports Chrome, Firefox, Edge.
- Rudimentary Gitlab.com tests and design for extension.

## Technology Stack

- **Language:** Java
- **Build Tool:** Gradle (Kotlin DSL)
- **Version Control:** Git

## Prerequisites

To build and run the application, you need the following installed:

- Java 21 or later
- Gradle (pre-configured with the Gradle wrapper)
- Git

## Setup Instructions

1. **Build the project:**
   Use the Gradle wrapper to build the application.
   ```bash
   ./gradlew build
   ```

2. **Run tests:**
   Execute unit tests with:
   ```bash
   ./gradlew test
   ```

### Adding Secrets

1. Go to the GitHub repository.
2. Navigate to **Settings > Secrets and variables > Actions**.
3. Click **New repository secret** and add the following secrets:

- `EMAIL`
- `PASSWORD`
- `API_TOKEN`
