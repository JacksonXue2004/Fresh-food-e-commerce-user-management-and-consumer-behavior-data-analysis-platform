# Fresh Data Management Platform

This repository contains a streamlined RuoYi-based solution that focuses on two business domains:

1. **User management:** account lifecycle, role authorization, and operational auditing.
2. **Consumer-data analytics:** dashboards that surface behavioral metrics sourced from curated CSV inputs and SQL views.

The implementation keeps the familiar RuoYi technology stack while trimming unused demos so the README tracks the current scope.

## Table of Contents

- [Features](#features)
- [Tech Stack](#tech-stack)
- [Repository Layout](#repository-layout)
- [Getting Started](#getting-started)
- [Configuration](#configuration)
- [Database Setup](#database-setup)
- [Build & Run](#build--run)
- [Troubleshooting](#troubleshooting)
- [Contributing](#contributing)
- [License](#license)
- [Acknowledgements](#acknowledgements)

## Features

- **Centralized user management:** account CRUD, role assignments, department hierarchy, and permission control powered by Apache Shiro.
- **Consumer behavior analytics:** curated dashboards (ECharts + Bootstrap) that visualize trends sourced from `app_data_cleaned.csv` and the analytical SQL scripts under `数据库脚本/`.
- **Operations visibility:** login/operation logging, parameter dictionaries, and menu management aligned with the RuoYi back-office ergonomics.

## Tech Stack

- **Backend:** Spring Boot 2.5.15, Spring Framework 5.3.39, Apache Shiro, MyBatis, Druid connection pool.
- **Frontend:** Thymeleaf templates with the RuoYi UI kit (Bootstrap, jQuery, ECharts).
- **Data storage:** MySQL 5.7+.
- **Build & tooling:** Maven 3.6+, JDK 8, Velocity templates for internal code scaffolding, Ehcache for session caching.

## Repository Layout

```
freshdata-admin/      # Web application (controllers, views, static resources)
freshdata-common/     # Domain objects, DTOs, utilities shared by all modules
freshdata-framework/  # Core configuration (security, filters, logging)
freshdata-generator/  # Internal scaffolding templates (kept for dev use)
freshdata-quartz/     # Quartz job definitions; optional, only build if needed
freshdata-system/     # Business services (users, menus, parameter configs)
bin/                  # Windows helper scripts (build, run, clean)
数据库脚本/              # SQL schema + data for analytics dashboards
app_data_cleaned.csv  # Sample dataset for consumer analytics
pom.xml               # Parent Maven configuration
README.md             # Project overview (this file)
```

## Getting Started

### Prerequisites

- JDK 8 (set `JAVA_HOME` accordingly)
- Maven 3.6 or later
- MySQL 5.7+ with a user that can create databases

### Build & package

```bash
git clone https://github.com/<your-account>/<repo>.git
cd <repo>
mvn clean package -DskipTests
```

The assembled Spring Boot executable is generated at `freshdata-admin/target/freshdata-admin.jar`.

## Configuration

Key configuration files within `freshdata-admin/src/main/resources/`:

- `application.yml`: base application settings, including the default datasource URL (`ry-vue` DB).
- `application-druid.yml`: Druid-specific connection-pool tuning.
- `logback.xml`: logging levels and appenders.

Update `spring.datasource.url`, `spring.datasource.username`, and `spring.datasource.password` to point at your MySQL instance. Add extra Spring profiles (e.g., `application-dev.yml`) if you need environment overrides.

## Database Setup

1. Create the target database (default `ry-vue`).
2. Run the scripts inside `数据库脚本/`:
   - `ry_20250416.sql` seeds the schema, menus, and admin account (`admin` / `admin123`).
   - Apply optional deltas such as `add_freshdata_menus.sql`, `complete_import.sql`, or cleanup scripts depending on your deployment plan.
3. Import `app_data_cleaned.csv` wherever your analytics ETL expects it (CSV copy exists under both repo root and `freshdata-admin/src/main/resources/`).

## Build & Run

### Development

```bash
cd freshdata-admin
mvn spring-boot:run
```

Visit `http://localhost:8080/` and sign in with the credentials provisioned by the SQL scripts.

### Production

```bash
mvn clean package -Pprod
java -jar freshdata-admin/target/freshdata-admin.jar --spring.profiles.active=prod
```

Ensure the `prod` profile provides the correct datasource, file storage, and security hardening before exposing the service.

## Troubleshooting

- **Login failures:** verify the `sys_user` table and confirm password encryption matches the seeded admin account.
- **Static assets missing:** rerun `mvn clean package`; the `static/` directory should be bundled under `freshdata-admin/target/classes/`.
- **Analytics data empty:** confirm CSV imports and that the `complete_import.sql` script has been executed.
- **Port conflict:** change `server.port` inside `application.yml` if 8080 is occupied.

## Contributing

Internal-only contributions should follow the existing package naming, controller/service layering, and code formatting. When adding analytics widgets, accompany template changes with the relevant SQL or CSV documentation so the dashboards stay reproducible.

## License

The project inherits the upstream [RuoYi license](https://gitee.com/y_project/RuoYi/blob/master/LICENSE). Verify compatibility with your organization before deploying.

## Acknowledgements

- [RuoYi](https://gitee.com/y_project/RuoYi) for the base framework and UI theme.
- Teammates who curated the consumer-data dataset and dashboard designs.
