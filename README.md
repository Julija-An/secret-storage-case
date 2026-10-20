# Case: Secret Storage

## Background

You've just joined a small team that has built a simple internal service. The application is a Spring Boot web app that requires authentication to access. It's packaged as a Docker container and run locally.

Your tech lead has asked you to take a look at the repository and address any security concerns you find with how the application is configured — particularly around how secrets are handled.

## Your Task

1. **Identify the security problem** in this repository.
2. **Fix it.** Implement a solution that resolves the issue.
3. **Describe your reasoning.** Briefly explain why the current approach is problematic and how your solution addresses it. If your ideal solution involves infrastructure or tooling beyond this repo, feel free to describe what that would look like — you don't need to build it, but show us your thinking.

## Running the Application

Build and run with Docker:

```sh
docker build -t secret-case .
docker run -p 8080:8080 secret-case
```

Then visit [http://localhost:8080](http://localhost:8080). You'll be prompted for credentials.

- **Username:** `admin`
- **Password:** *(check the source code)*

Or test with curl:

```sh
curl -u admin:<password> http://localhost:8080
```

## What We're Looking For

- Can you spot the problem?
- Is your solution practical and well-reasoned?
- How do you think about this in a broader context (local development, CI/CD, production)?
- No more than 15 minutes presentation on your solution and thoughts

There is no single "right" answer. We want to see how you think.

## Time Expectation

This should not take long. Don't over-engineer it. A clean, simple fix with clear reasoning is preferred over a complex one. You can hand-wave external services that might be part of your ideal design.


<h1 align="center"> 🚨 Emergency solution for VERY "secret" password </h1>

 > The password was in the repo like a spare key under the doormat. 
 > Now app asks for it at run time and Git never sees it.

## The problem
The admin password was hardcoded in the souce code `application.properties`.
This is bad because:
- **Compromised password.** Delete password from file but it's still in Git's history.
- **Everyone knows.** It can be used by anyone in prod, dev, test environment.
- **It's inside Docker image.** If you get docker image and unzip the jar you can see it.
- **Redeploy** If you change the password you need to change it in source code and then rebuild, redeploy.

### 🚑 What was done
- Removed the hardcoded admin password from `application.properties`. So password is now environmental variable.
- Added `.env.example` with variable only name.
- Added `.env` to `.gitignore` and `.dockerignore`

## Run the Application

Build and run with Docker:

```sh
docker build -t secret-case .
docker run -p 8080:8080 -e ADMIN_PASSWORD='nice_long_random_password' secret-case
```

<h1 align="center"> 🚨 Solved other problems </h1>

> curl -i -H 'Authorization: Basic !!!' http://localhost:8080 returned HTTP/1.1 500  - > now  returns 401

> Added UTF-8 so the login text is decoded as UTF-8 and stay the same on all computers.

> Added basic test for login
```sh
docker run --rm -v "$PWD":/app -w /app maven:3.9.9-eclipse-temurin-21 mvn test
```

<h1 align="center"> Other things to change in future</h1>

> Replace basic login with Spring Security as it handless most problems on its own.