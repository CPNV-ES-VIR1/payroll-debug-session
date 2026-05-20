const sqlite3 = require('sqlite3').verbose();
const path = require('path');

let db;

function connectDb() {
    return new Promise((resolve, reject) => {
        const dbFile = process.env.NODE_ENV === 'test' ? ':memory:' : path.join(__dirname, '..', './data/customers.sqlite');
        db = new sqlite3.Database(dbFile, (err) => {
            if (err) return reject(err);
            resolve(db);
        });
    });
}

function getDb() {
    if (!db) {
        throw new Error("Database not initialized. Call connectDb() first.");
    }
    return db;
}

function initDb() {
    return new Promise((resolve, reject) => {
        const initSql = `
            PRAGMA foreign_keys = ON;

            CREATE TABLE IF NOT EXISTS customers (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT NOT NULL,
                firstname TEXT NOT NULL,
                phoneNumber TEXT NOT NULL,
                emailAddress TEXT NOT NULL UNIQUE,
                created_at TEXT DEFAULT (datetime('now')),
                updated_at TEXT DEFAULT (datetime('now'))
            );
        `;
        db.exec(initSql, (err) => {
            if (err) return reject(err);
            resolve();
        });
    });
}

function seedDb() {
    return new Promise((resolve, reject) => {
        const db = getDb();

        const customers = [
            ['Davis', 'Elijah', '+1-944-867-1271', 'elijah.davis@mail.com'],
            ['Harris', 'Hannah', '+1-692-603-8405', 'hannah.harris@testmail.org'],
            ['Jackson', 'Sophia', '+1-784-336-8217', 'sophia.jackson@mail.com'],
            ['Anderson', 'Emma', '+1-620-679-4966', 'emma.anderson@testmail.org']
        ];

        const stmt = db.prepare(
            `
            INSERT OR IGNORE INTO customers (name, firstname, phoneNumber, emailAddress)
            VALUES (?, ?, ?, ?)
            `
        );

        for (const customer of customers) {
            stmt.run(customer, (err) => {
                if (err) {
                    stmt.finalize();
                    return reject(err);
                }
            });
        }

        stmt.finalize((err) => {
            if (err) return reject(err);
            resolve();
        });
    });
}


function closeDb() {
    return new Promise((resolve, reject) => {
        if (!db) return resolve();
        db.close(err => {
            if (err) return reject(err);
            resolve();
        });
    });
}

module.exports = { connectDb, getDb, initDb, seedDb, closeDb };
