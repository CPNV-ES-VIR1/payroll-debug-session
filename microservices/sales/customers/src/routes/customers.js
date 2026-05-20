const express = require('express');
const { body, param } = require('express-validator');
const customerController = require('../controllers/customer');

const router = express.Router();

/**
 * @openapi
 * /api/v1/customers:
 *   get:
 *     summary: Retrieve a list of customers
 *     tags:
 *       - Customers
 *     responses:
 *       200:
 *         description: A list of customers
 *         content:
 *           application/json:
 *             schema:
 *               type: array
 *               items:
 *                 type: object
 *                 properties:
 *                   id:
 *                     type: integer
 *                   name:
 *                     type: string
 *                   firstname:
 *                     type: string
 *                   phoneNumber:
 *                     type: string
 *                   emailAddress:
 *                     type: string
 *                   created_at:
 *                     type: string
 *                   updated_at:
 *                     type: string
 *   post:
 *     summary: Create a new customer
 *     tags:
 *       - Customers
 *     requestBody:
 *       required: true
 *       content:
 *         application/json:
 *           schema:
 *             type: object
 *             required:
 *               - name
 *               - firstname
 *               - phoneNumber
 *               - emailAddress
 *             properties:
 *               name:
 *                 type: string
 *               firstname:
 *                 type: string
 *               phoneNumber:
 *                 type: string
 *               emailAddress:
 *                 type: string
 *     responses:
 *       201:
 *         description: Customer created
 *       409:
 *         description: Duplicate customer (unique constraint violation)
 *
 * /api/v1/customers/{id}:
 *   get:
 *     summary: Get a customer by ID
 *     tags:
 *       - Customers
 *     parameters:
 *       - in: path
 *         name: id
 *         required: true
 *         schema:
 *           type: integer
 *     responses:
 *       200:
 *         description: A single customer
 *       404:
 *         description: Customer not found
 *   put:
 *     summary: Upsert a customer by ID (update or create)
 *     tags:
 *       - Customers
 *     parameters:
 *       - in: path
 *         name: id
 *         required: true
 *         schema:
 *           type: integer
 *     requestBody:
 *       required: true
 *       content:
 *         application/json:
 *           schema:
 *             type: object
 *             required:
 *               - name
 *               - firstname
 *               - phoneNumber
 *               - emailAddress
 *             properties:
 *               name:
 *                 type: string
 *               firstname:
 *                 type: string
 *               phoneNumber:
 *                 type: string
 *               emailAddress:
 *                 type: string
 *     responses:
 *       200:
 *         description: Customer updated
 *       201:
 *         description: Customer created
 *       409:
 *         description: Duplicate customer (unique constraint violation)
 *   delete:
 *     summary: Delete a customer by ID
 *     tags:
 *       - Customers
 *     parameters:
 *       - in: path
 *         name: id
 *         required: true
 *         schema:
 *           type: integer
 *     responses:
 *       204:
 *         description: Customer deleted
 *       404:
 *         description: Customer not found
 */

/**
 * Validation rules
 */
const createAndUpdateValidationsCustomer = [
    body('name').isString().notEmpty().withMessage('name is required'),
    body('firstname').isString().notEmpty().withMessage('firstname is required'),
    body('phoneNumber').isString().notEmpty().withMessage('phoneNumber is required'),
    body('emailAddress').isString().notEmpty().withMessage('emailAddress is required')
];

/**
 * Routes
 */
router.get('/', customerController.getAll);

router.get(
    '/:id',
    [param('id').isInt().withMessage('id must be an integer')],
    customerController.getById
);

router.post(
    '/',
    createAndUpdateValidationsCustomer,
    customerController.create
);

router.put(
    '/:id',
    [param('id').isInt().withMessage('id must be an integer'), ...createAndUpdateValidationsCustomer],
    customerController.upsert
);

router.delete(
    '/:id',
    [param('id').isInt().withMessage('id must be an integer')],
    customerController.delete
);

module.exports = router;
