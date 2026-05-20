// config/swagger.js
const swaggerJSDoc = require('swagger-jsdoc');
require('dotenv').config();

const port = process.env.PORT || 3000;

const options = {
    definition: {
        openapi: '3.0.0',
        info: {
            title: 'Customers API',
            version: '1.0.0',
            description: 'RESTful API for customers management (SQLite, Express).'
        },
        servers: [
            {
                url: `http://localhost:${port}`,
                description: 'Local development server'
            },
        ],
    },
    apis: ['./src/routes/*.js', './src/controllers/*.js'] // pick up JSDoc in routes/controllers
};

const swaggerSpec = swaggerJSDoc(options);
module.exports = swaggerSpec;