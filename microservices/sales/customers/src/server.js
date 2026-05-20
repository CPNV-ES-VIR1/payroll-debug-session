require('dotenv').config();

const { connectDb, initDb, seedDb } = require('./config/database');

async function startServer() {
    try {
        console.log('Initializing database...');
        await connectDb();
        await initDb();
        await seedDb();

        console.log('Database ready.');

        const app = require('./app');
        const port = process.env.PORT || 3000;
        app.listen(port, () => {
            console.log(`Server listening on http://localhost:${port}`);

            if (process.env.NODE_ENV !== 'production') {
                console.log(`Swagger UI: http://localhost:${port}/docs`);
            }
        });

    } catch (err) {
        console.error('Failed to start server:', err);
        process.exit(1);
    }
}

// Prevent Jest from auto-starting the server
if (process.env.NODE_ENV !== 'test') {
    startServer();
}

module.exports = startServer;
