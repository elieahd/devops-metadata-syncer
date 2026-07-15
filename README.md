# DevOps Platform

![java-26](https://img.shields.io/badge/java-26-red)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=elieahd_devops-metadata-syncer&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=elieahd_devops-metadata-syncer)
[![codecov](https://codecov.io/gh/elieahd/devops-metadata-syncer/graph/badge.svg?token=PKuICGh2k3)](https://codecov.io/gh/elieahd/devops-metadata-syncer)

A tool that pull DevOps metadata (Pull Requests, Pipelines, Releases, Vulnerabilities) from multiple repositories given
a project and also allow to attach checks reports to projects

## Main operations via Endpoints

| Endpoints                                                   | Description                                      |
|-------------------------------------------------------------|--------------------------------------------------|
| `PUT` `/api/v1/projects/{projectKey}/do-sync`               | Synchronize all repositories for a given project |
| `POST` `/api/v1/projects/{projectKey}/reports/{reportType}` | Add report for a given project                   |

## Data Model

The following class diagram describes the internal data structures of a Project

```mermaid
classDiagram
    direction LR

    class Project {
        +Long id
        +String key
        +String name
    }

    class Repository {
        +Long id
        +String organization
        +String name
        +RepositorySource source
        +LocalDateTime lastSyncTime
    }

    class RepositorySource {
        <<enumeration>>
        GITHUB
        ENTERPRISE_GITHUB
        UNKNOWN
    }

    class Pipeline {
        +String name
        +String sourceId
        +List~PipelineRun~ runs
    }

    class PipelineRun {
        +boolean success
        +OffsetDateTime startedAt
        +OffsetDateTime createdAt
        +OffsetDateTime updatedAt
        +Duration duration
    }

    class PullRequest {
        +int number
        +String title
        +String state
        +OffsetDateTime publishedAt
        +OffsetDateTime mergedAt
        +OffsetDateTime closedAt
        +String author
        +boolean isAuthorUser
        +List~PullRequestReview~ reviews
    }

    class PullRequestReview {
        +String reviewer
        +String state
        +OffsetDateTime submittedAt
    }

    class Release {
        +String name
        +String tagName
        +OffsetDateTime publishedAt
    }

    class Vulnerability {
        +String artifact
        +String impactedVersion
        +String state
        +OffsetDateTime createdAt
        +OffsetDateTime resolvedAt
    }

    class Report {
        +ReportType type
        +ReportStatus status
        +LocalDateTime createdAt
        +String metadata
    }

    class ReportType {
        <<enumeration>>
        MORNING_CHECK
    }

    class ReportStatus {
        <<enumeration>>
        SUCCESS
        FAILED
    }

    Project "1" --> "*" Repository: has
    Project "1" --> "*" Report: has
    Repository "1" --> "*" PullRequest: has
    Repository "1" --> "*" Pipeline: has
    Repository "1" --> "*" Vulnerability: has
    Repository "1" --> "*" Release: has
    Pipeline "1" --> "*" PipelineRun: contains
    PullRequest "1" --> "*" PullRequestReview: has
    Repository "*" --> "1" RepositorySource: source
    Report "*" --> "1" ReportStatus: status
    Report "*" --> "1" ReportType: type
```

## Pipelines

| Event            | Description                                                                | Pipeline                                                                                                                                                                                                                                | 
|------------------|----------------------------------------------------------------------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `push` on `main` | Pre-Checks (test + codecov + sonar)                                        | [![🚀 Deploy](https://github.com/elieahd/devops-metadata-syncer/actions/workflows/deploy.yaml/badge.svg)](https://github.com/elieahd/devops-metadata-syncer/actions/workflows/deploy.yaml)                                              |
| `pull request`   | Checks (test + sonar)                                                      | [![✅ PR checks](https://github.com/elieahd/devops-metadata-syncer/actions/workflows/pr-checks.yaml/badge.svg)](https://github.com/elieahd/devops-metadata-syncer/actions/workflows/pr-checks.yaml)                                      |
| `weekly`         | Dependabot updates <br/> maintaining maven and github actions dependencies | [![Dependabot Updates](https://github.com/elieahd/devops-metadata-syncer/actions/workflows/dependabot/dependabot-updates/badge.svg)](https://github.com/elieahd/devops-metadata-syncer/actions/workflows/dependabot/dependabot-updates) |
