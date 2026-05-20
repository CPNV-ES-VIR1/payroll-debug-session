const CustomerService = require('../services/customer');
const {
    CustomerAlreadyExistsError,
    CustomerNotFoundError
} = require('../errors/BusinessErrors');

const CustomerController = {

    /*
    curl -X GET http://localhost:3000/api/v1/customers ^
    -H "Accept: application/json" ^
    | jq
     */
    async getAll(req, res, next) {
        try {
            const customers = await CustomerService.getAll();
            res.status(200).json(customers);
        } catch (err) {
            next(err);
        }
    },

    /*
    curl -X GET http://localhost:3000/api/v1/customers/1 ^
      -H "Accept: application/json" ^
      | jq
     */
    async getById(req, res, next) {
        try {
            const { id } = req.params;
            const customer = await CustomerService.getById(Number(id));
            res.status(200).json(customer);
        } catch (err) {
            if (err instanceof CustomerNotFoundError) {
                return res.status(404).json({ message: err.message });
            }
            next(err);
        }
    },

    /*
    curl -X POST http://localhost:3000/api/v1/customers ^
      -H "Content-Type: application/json" ^
      -d "{\"name\":\"Doe\",\"firstname\":\"John\",\"phoneNumber\":\"+1-111-222-3333\",\"emailAddress\":\"john.doe@test.com\"}"
    */
    async create(req, res, next) {
        try {
            const customer = await CustomerService.create(req.body);
            res.status(201).json(customer);
        } catch (err) {
            if (err instanceof CustomerAlreadyExistsError) {
                return res.status(409).json({ message: err.message });
            }
            next(err);
        }
    },

    /*
    curl -X PUT http://localhost:3002/api/v1/customers/1 ^
        -H "Content-Type: application/json" ^
        -d "{\"name\":\"Doe\",\"firstname\":\"Johnny\",\"phoneNumber\":\"+1-111-222-4444\",\"emailAddress\":\"johnny.doe@test.com\"}"
    */
    async upsert(req, res, next) {
        try {
            const { id } = req.params;

            // Call service
            const { customer, created } = await CustomerService.upsert(Number(id), req.body);

            // Respond with proper status
            if (created) {
                return res.status(201).json(customer);
            }

            return res.status(200).json(customer);
        } catch (err) {
            if (err instanceof CustomerAlreadyExistsError) {
                return res.status(409).json({ message: err.message });
            }
            next(err);
        }
    },

    /*
    curl -X DELETE http://localhost:3000/api/v1/customers/1
     */
    async delete(req, res, next) {
        try {
            const { id } = req.params;
            await CustomerService.delete(Number(id));
            res.status(204).send();
        } catch (err) {
            if (err instanceof CustomerNotFoundError) {
                return res.status(404).json({ message: err.message });
            }
            next(err);
        }
    }
};

module.exports = CustomerController;
