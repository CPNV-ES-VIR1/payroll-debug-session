const { getDb } = require('../config/database');

const Customer = {

    // READ ALL
    findAll() {
        const db = getDb();
        return new Promise((resolve, reject) => {
            db.all('SELECT * FROM customers', (err, rows) => {
                if (err) reject(err);
                else resolve(rows);
            });
        });
    },

    // READ ONE
    findById(id) {
        const db = getDb();
        return new Promise((resolve, reject) => {
            db.get(
                'SELECT * FROM customers WHERE id = ?',
                [id],
                (err, row) => {
                    if (err) reject(err);
                    else resolve(row);
                }
            );
        });
    },

    // CREATE
    create(customer) {
        const db = getDb();
        const { name, firstname, phoneNumber, emailAddress } = customer;

        return new Promise((resolve, reject) => {
            db.run(
                `
                INSERT INTO customers (name, firstname, phoneNumber, emailAddress)
                VALUES (?, ?, ?, ?)
                `,
                [name, firstname, phoneNumber, emailAddress],
                function (err) {
                    if (err) reject(err);
                    else resolve({
                        id: this.lastID,
                        name,
                        firstname,
                        phoneNumber,
                        emailAddress
                    });
                }
            );
        });
    },

    // UPDATE
    update(id, customer) {
        const db = getDb();
        const { name, firstname, phoneNumber, emailAddress } = customer;

        return new Promise((resolve, reject) => {
            db.run(
                `
                UPDATE customers
                SET name = ?,
                    firstname = ?,
                    phoneNumber = ?,
                    emailAddress = ?,
                    updated_at = datetime('now')
                WHERE id = ?
                `,
                [name, firstname, phoneNumber, emailAddress, id],
                function (err) {
                    if (err) reject(err);
                    else resolve(this.changes > 0);
                }
            );
        });
    },

    // DELETE
    delete(id) {
        const db = getDb();

        return new Promise((resolve, reject) => {
            db.run(
                'DELETE FROM customers WHERE id = ?',
                [id],
                function (err) {
                    if (err) reject(err);
                    else resolve(this.changes > 0);
                }
            );
        });
    }
};

module.exports = Customer;
