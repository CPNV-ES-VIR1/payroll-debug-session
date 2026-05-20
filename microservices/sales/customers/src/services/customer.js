const CustomerEntity = require('../entities/Customer');
const {
    CustomerAlreadyExistsError,
    CustomerNotFoundError
} = require('../errors/BusinessErrors');

const CustomerService = {

    async getAll() {
        return CustomerEntity.findAll();
    },

    async getById(id) {
        const customer = await CustomerEntity.findById(id);
        if (!customer) {
            throw new CustomerNotFoundError(`Customer ${id} not found`);
        }
        return customer;
    },

    async create(customer) {
        try {
            return await CustomerEntity.create(customer);
        } catch (err) {
            if (err.code === 'SQLITE_CONSTRAINT') {
                throw new CustomerAlreadyExistsError(
                    'Customer violates a unique constraint'
                );
            }
            throw err;
        }
    },

    async upsert(id, payload) {
        try {
            const existing = await CustomerEntity.findById(id);

            if (existing) {
                return {
                    created: false,
                    customer: await CustomerEntity.update(id, payload)
                };
            }

            return {
                created: true,
                customer: await CustomerEntity.create({ id, ...payload })
            };

        } catch (err) {
            if (err.code === 'SQLITE_CONSTRAINT') {
                throw new CustomerAlreadyExistsError(
                    'Customer conflicts with an existing resource'
                );
            }
            throw err;
        }
    },


    async delete(id) {
        const existing = await CustomerEntity.findById(id);
        if (!existing) {
            throw new CustomerNotFoundError(`Customer ${id} not found`);
        }

        await CustomerEntity.delete(id);
    }
};

module.exports = CustomerService;
