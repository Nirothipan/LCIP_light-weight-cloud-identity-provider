# Introduction

Identity and Access Management has become an inevitable component of any application. Application developers spend a considerable amount of their development time to fulfill the tasks related to user authentication and authorization. Such tasks include; User Authentication, User Authorization, Application Management, Tenant Management. The main objective of this project is to develop an application which provides Identity and
Access Management targeting the application developers to ease the development process. Also the solution is implemented as a cloud native application leveraging the existing cloud
technologies mainly from Amazon.

## High Level Architecture

![Architecture](images/architecture.png)

## Solution Approach

Lightweight Cloud Identity Provider (LCIP) is developed using a 3-Tier architecture. The solution
consists of 3 main components:
1. Presentation Layer
2. Application Layer
3. Persistence Layer

![layered archtiecture](images/solution-approach.png)


## Components

### LCIP BackEnd Lambdas

### LCIP UIs

## CICD Pipeline

![CI/CD Diagram](images/cicd-diagram.png)

We have two different CI/CD pipeline for
1. Backend deployment
2. Front end deployment

Both deployments will get triggered once we push the code to the github.
Codepipeline will do the maven build and update the S3 with the latest jars. Then lambdas will
be redeployed with the new jars. Similarity Amplify will handle the front end(portal) builds using
node and get redeployed.

## Observability

Observability can be viewed as a superset of monitoring where monitoring is enriched
with capabilities to perform debugging and profiling through rich context, log analysis,
correlation, and tracing. Modern day observability resides on three pillars of logs, metrics, and
tracing. Modern businesses require observability systems to self-sufficiently emit their current
state(overview), generate alerts for any abnormalities detected to proactively identify failures,
and to provide information to find the root causes of a system failure.
We have added Observability capabilities for our product using below AWS services.

![Observability Diagram](images/observability-diagram.png)
