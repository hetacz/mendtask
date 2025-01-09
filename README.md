# Mend.io Java Development Task

## Overview

This is a repo with a Java Development Task assignment solution.

## Features

- Parallelized sanity tests of GitHub.com 4 MVP capabilities
    - Login
    - Create a new repository
    - Delete a repository
    - Get billing information
- Each step is first done by UI automation, then performance is optimized through API requests
- Properties and Parameters are read from (in order):
    - GitHub Secrets
    - `.xml` files in `src/test/resources`
    - System Properties (best for parallelized execution)
    - `config.properties` in `src/java/resources`
    - Default values
- Supports Chrome, Firefox, Edge.
- Rudimentary Gitlab.com tests and design for extension.

## Technology Stack

- **Language:** Java
- **Build Tool:** Gradle (Kotlin DSL)
- **Version Control:** Git
- **CI/CD:** GitHub Actions

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

## CI/CD with GitHub Actions

GitHub Actions is configured to automate testing, building, and deployment processes. The workflow is defined in
`.github/workflows/build.yml`.

## Secrets Management

Sensitive data such as email credentials, API tokens, and passwords are stored as GitHub Secrets. These are referenced
in the workflow to ensure security.

### Adding Secrets

1. Go to the GitHub repository.
2. Navigate to **Settings > Secrets and variables > Actions**.
3. Click **New repository secret** and add the following secrets:

- `github.email`: Email address for notifications.
- `github.password`: Password for secure access.
- `github.token`: API token for integration.
