# ToDoApp

## Project Summary

ToDoApp is little app that implements a simple personal task manager. With it you can create your own to do list and keep track of what remains to be done.

## Built with

This project was developed using Spring Boot + Maven with authentication performed using JWT Authentification. The database is managed through MySql using the Hibernate framework.

## Usage

### User management

Under the path `/users` the app exposes three end points (`/signup`, `/login`, `/delete`) to, respectively, sign up new users, log in as an existing user and delete an user.

The signing up and logging in `POST`request body follows the structure: 

```json
{
  "username": "foo1",
  "password": "foo2"
}
```

For these two requests, a success response returns the logged user information along with a JWT token to be used as authorization.

Finally, the deletion request is a simple `GET` request with a valid token as Authentification Header.

### Task Management

The managing of the user's tasks is done under the path `/tasks`. For each request, a valid JWT token must be provided in the request's Authorization Header.

- `/tasks`      - `GET` request to fetch a summarized list of all tasks;
- `/tasks`      - `POST`request to add new task;
- `/tasks/{id}` - `GET` request to fetch the full information about a task;
- `/tasks/{id}` - `PUT` request to update an existing task;
- `/tasks/{id}` - `DELETE` request to delete an existing task.

When adding or updating a task the Request Body follows the structure:

```json
{
  "title": "foo1",
  "description": "foo2",
  "completed": true
}
```
