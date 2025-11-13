# Fresh Data Management Platform

This repository hosts a customized build of the **RuoYi** ecosystem that powers a user management and consumer behavior analytics platform. The project aggregates operational data, provides rich dashboards, and supports rule-based scheduling via Quartz.

## Table of Contents

- [Features](#features)
- [Tech Stack](#tech-stack)
- [Project Structure](#project-structure)
- [Getting Started](#getting-started)
- [Configuration](#configuration)
- [Database Setup](#database-setup)
- [Build & Deployment](#build--deployment)
- [Troubleshooting](#troubleshooting)
- [Contributing](#contributing)
- [License](#license)
- [Acknowledgements](#acknowledgements)

## Features

- User, role, and permission management with Shiro integration.
- Consumer behavior analytics dashboards and reports.
- Scheduled jobs powered by Quartz.
- Code generator for rapid CRUD scaffolding.
- Responsive front-end based on the RuoYi UI theme.
- Maven multi-module project for modular maintenance.

## Tech Stack

- **Backend:** Spring Boot 2.5, Spring Framework 5.3, Apache Shiro, MyBatis
- **Database:** MySQL (recommended 5.7+)
- **Scheduling:** Quartz
- **Build:** Maven 3.6+, Java 8
- **Front-end:** RuoYi UI (Thymeleaf, jQuery, Bootstrap)

## Project Structure

```
freshdata-admin/      # Web application (controllers, views, static resources)
freshdata-common/     # Shared utilities and domain models
freshdata-framework/  # Core framework extensions (config, security)
freshdata-generator/  # Code generator module
freshdata-quartz/     # Quartz job definitions and services
freshdata-system/     # System management services (users, menus, logs)
数据库脚本/              # SQL scripts for schema and seed data
bin/                  # Convenience scripts for Windows users
项目完整说明.md           # Extended Chinese documentation
如何开启注册功能.md        # Guide for enabling user registration
pom.xml               # Parent Maven build descriptor
README.md             # Project overview (this file)
.gitignore            # Git ignore rules
```

## Getting Started

### Prerequisites

- Java Development Kit (JDK) 8
- Maven 3.6 or later
- MySQL database server
- Node.js 14+ (optional, if you plan to rebuild front-end assets)

### Installation

```bash
# Clone the repository
git clone https://github.com/<your-account>/<repo>.git
cd <repo>

# Build all modules
mvn clean package -DskipTests
```

The assembled application JAR will be located under `freshdata-admin/target/`.

## Configuration

- **Application properties:** `freshdata-admin/src/main/resources/application.yml`
- **Datasource settings:** Update the `spring.datasource.*` entries with your MySQL credentials.
- **Redis (optional):** Configure Redis settings if you enable caching or session sharing.
- **Logging:** Adjust `logback.xml` under `freshdata-admin/src/main/resources/` to tailor logging levels and appenders.

Environment-specific overrides can be added by creating additional profiles such as `application-dev.yml` or `application-prod.yml`.

## Database Setup

1. Create a database (default name `ry-vue` or customize in `application.yml`).
2. Execute the SQL scripts located in `数据库脚本/` in the following order:
   - `ry_20250416.sql`
   - Any incremental scripts (`add_freshdata_menus.sql`, `complete_import.sql`, etc.) as required by your deployment.
3. Verify that core tables such as `sys_user`, `sys_role`, and analytics-specific tables exist.

## Build & Deployment

### Development Run

```bash
# Start the Spring Boot application
cd freshdata-admin
mvn spring-boot:run
```

Access the UI at `http://localhost:8080/`. Default credentials can be found in the SQL seed data (`admin/admin123` unless changed).

### Production Package

```bash
mvn clean package -Pprod
java -jar freshdata-admin/target/freshdata-admin.jar --spring.profiles.active=prod
```

Ensure that the production profile includes the correct datasource, Redis, and security settings.

## Troubleshooting

- **Login fails:** Check Shiro configuration and verify that the `sys_user` table contains the admin account.
- **Static resources missing:** Confirm that the `static` directory is bundled within `freshdata-admin/target/classes/`.
- **Quartz jobs not running:** Review Quartz job logs and database tables (`qrtz_*`). Make sure the scheduler is enabled in `application.yml`.
- **Port conflicts:** Adjust `server.port` in `application.yml` if 8080 is occupied.

## Contributing

Pull requests are welcome. For substantial changes:

1. Fork the repository and create a feature branch.
2. Ensure new code is covered by tests or manual verification.
3. Follow existing code style and document configuration changes.
4. Submit a PR with a clear description of enhancements or fixes.

## License

This project inherits licensing from the upstream RuoYi framework. See the original [RuoYi license](https://gitee.com/y_project/RuoYi/blob/master/LICENSE) for details. Confirm compliance with your organization’s policies before deploying.

## Acknowledgements

- [RuoYi](https://gitee.com/y_project/RuoYi) for the original framework and UI.
- Contributors who provided data analytics enhancements and documentation.


