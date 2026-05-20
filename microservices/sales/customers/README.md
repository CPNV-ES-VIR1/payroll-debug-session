# Customers API

A simple RESTful API to manage customers (CRUD) built with **Express**, **SQLite3**, **express-validator**, and documented with **Swagger UI**.

---

## Requirements

- **Node.js**: v18.x or higher
- **npm**: v9.x or higher

*(older versions may work but are not tested)*

---

## Project structure

```bash
│   .env
│   .gitignore
│   customers.sqlite
│   package-lock.json
│   package.json
│   README.md
│
├───docs
│       class_diagram.puml
│
└───src
    │   app.js
    │   server.js
    │
    ├───config
    │       database.js
    │       swagger.js
    │
    ├───controllers
    │       customer.js
    │
    ├───entities
    │       Customer.js
    │
    └───routes
            customers.js
            router.js
```

## Installation

Clone the repository, then install dependencies:

```bash
cd ms-customers
npm install
```

## Development

Start the server in dev mode (with auto-reload via nodemon):

```bash
npm run dev
```

Start the server normally:

```bash
npm start
```

## Usage

API base URL: http://localhost:3001/api

Swagger UI docs: http://localhost:3001/docs

## Environment

The .env file defines:

```bash
PORT=3002
DB_FILE=./dev.sqlite
NODE_ENV=development
```

