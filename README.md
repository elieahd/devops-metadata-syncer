# DevOps Metadata Syncer

![java-26](https://img.shields.io/badge/java-26-red)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=elieahd_devops-metadata-syncer&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=elieahd_devops-metadata-syncer)
[![codecov](https://codecov.io/gh/elieahd/devops-metadata-syncer/graph/badge.svg?token=PKuICGh2k3)](https://codecov.io/gh/elieahd/devops-metadata-syncer)

A tool that pull DevOps metadata (Pull Requests, Pipelines, Releases, Vulnerabilities) from multiple repositories given
a project

## Commands

Every invocation requires the following global argument:

| Argument    | Required | Description                        |
|-------------|----------|------------------------------------|
| `--command` | Yes      | The name of the command to execute |

### `sync-project`

Sync Metadata for a given project

**Arguments:**

| Argument    | Required | Type   | Description            |
|-------------|----------|--------|------------------------|
| `--project` | Yes      | String | The key of the project |

**Usage:**

```
--command=sync-project --project=<project>
```

```bash
java -jar devops-metadata-syncer.jar \
  --command=sync-project \
  --name=MyProject 
```

### `sync-repository`

Sync Metadata for a given repository

**Arguments:**

| Argument        | Required | Type   | Description                                           |
|-----------------|----------|--------|-------------------------------------------------------|
| `--organiation` | Yes      | String | The repository organization                           |
| `--repository`  | Yes      | String | The repository name                                   |
| `--source`      | No       | String | The repository Source (GitHub, EnterpriseGitHub, ...) |

**Usage:**

```
--command=sync-repository --organization=<org> --repository=<repository> --source=<source>
```

```bash
java -jar devops-metadata-syncer.jar \
  --command=sync-repository \
  --organization=<org> \
  --repository=<repository> \ 
  --source=<source>
```

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

    Project "1" --> "*" Repository: has
    Repository "1" --> "*" PullRequest: has
    Repository "1" --> "*" Pipeline: has
    Repository "1" --> "*" Vulnerability: has
    Repository "1" --> "*" Release: has
    Pipeline "1" --> "*" PipelineRun: contains
    PullRequest "1" --> "*" PullRequestReview: has
    Repository "1" --> "1" RepositorySource: source
```

## Pipelines

| Event            | Description                                                                | Pipeline                                                                                                                                                                                                                                | 
|------------------|----------------------------------------------------------------------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `push` on `main` | Pre-Checks (test + codecov + sonar)                                        | [![🚀 Deploy](https://github.com/elieahd/devops-metadata-syncer/actions/workflows/deploy.yaml/badge.svg)](https://github.com/elieahd/devops-metadata-syncer/actions/workflows/deploy.yaml)                                              |
| `pull request`   | Checks (test + sonar)                                                      | [![✅ PR checks](https://github.com/elieahd/devops-metadata-syncer/actions/workflows/pr-checks.yaml/badge.svg)](https://github.com/elieahd/devops-metadata-syncer/actions/workflows/pr-checks.yaml)                                      |
| `weekly`         | Dependabot updates <br/> maintaining maven and github actions dependencies | [![Dependabot Updates](https://github.com/elieahd/devops-metadata-syncer/actions/workflows/dependabot/dependabot-updates/badge.svg)](https://github.com/elieahd/devops-metadata-syncer/actions/workflows/dependabot/dependabot-updates) |
