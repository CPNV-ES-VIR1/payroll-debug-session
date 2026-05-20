class BusinessErrors extends Error {}

class CustomerNotFoundError extends BusinessErrors {}
class CustomerAlreadyExistsError extends BusinessErrors {}

module.exports = {
    CustomerNotFoundError,
    CustomerAlreadyExistsError
};
