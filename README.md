# GitHub Repositories API

A simple Spring Boot application that integrates with the GitHub API to fetch non-forked repositories for a given user, along with branch and commit data.

## API Usage

### Get user repositories

GET /api/v1/repositories/{username}

If the user is not found:
{
"status": 404,
"message": "User not found"
}
