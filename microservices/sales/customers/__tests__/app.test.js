const { connectDb, initDb, seedDb, closeDb } = require('../src/config/database');
const request = require('supertest');
const app = require('../src/app');

beforeAll(async () => {
    await connectDb();
    await initDb();
    await seedDb();
});

afterAll(async () => {
    await closeDb();
});

describe('Health endpoint', () => {
    it('GET /health should return status OK', async () => {
        const res = await request(app).get('/health');

        expect(res.statusCode).toBe(200);
        expect(res.body).toEqual({ status: 'OK' });
    });
});

describe('Customers API', () => {

    it('GET /customers should return seeded customers', async () => {
        const res = await request(app).get('/api/v1/customers');

        expect(res.statusCode).toBe(200);
        expect(Array.isArray(res.body)).toBe(true);
        expect(res.body.some(c => c.firstname === 'Elijah')).toBe(true);
    });

    it('POST /customers should create a new customer', async () => {
        const payload = {
            name: 'Doe',
            firstname: 'John',
            phoneNumber: '+1-111-222-3333',
            emailAddress: 'john.create@test.com'
        };

        const res = await request(app)
            .post('/api/v1/customers')
            .send(payload);

        expect(res.statusCode).toBe(201);
        expect(res.body).toHaveProperty('id');
        expect(res.body.firstname).toBe(payload.firstname);
    });

    it('POST /customers should return 409 on duplicate email (unique constraint)', async () => {
        const payload = {
            name: 'Dup',
            firstname: 'User',
            phoneNumber: '+1-999-888-7777',
            emailAddress: 'duplicate@test.com'
        };

        // GIVEN: customer already exists
        await request(app)
            .post('/api/v1/customers')
            .send(payload)
            .expect(201);

        // WHEN: creating the same customer again
        const res = await request(app)
            .post('/api/v1/customers')
            .send(payload);

        // THEN
        expect(res.statusCode).toBe(409);
        expect(res.body).toHaveProperty('message');
    });

    it('GET /customers/:id should return 404 if not found', async () => {
        const res = await request(app)
            .get('/api/v1/customers/999999');

        expect(res.statusCode).toBe(404);
    });

    it('PUT /customers/:id should update an existing customer', async () => {
        // GIVEN
        const createRes = await request(app)
            .post('/api/v1/customers')
            .send({
                name: 'Update',
                firstname: 'Target',
                phoneNumber: '123',
                emailAddress: 'update@test.com'
            });

        const id = createRes.body.id;

        // WHEN
        const res = await request(app)
            .put(`/api/v1/customers/${id}`)
            .send({
                name: 'Update',
                firstname: 'Updated firstname',
                phoneNumber: '456',
                emailAddress: 'updated@test.com'
            });

        // THEN
        expect(res.statusCode).toBe(200);
    });

    it('PUT /customers/:id should create customer if not exists (upsert)', async () => {
        const payload = {
            name: 'Upsert',
            firstname: 'Created',
            phoneNumber: '000',
            emailAddress: 'upsert@test.com'
        };

        const res = await request(app)
            .put('/api/v1/customers/999998')
            .send(payload);

        expect(res.statusCode).toBe(201);
        expect(res.body).toHaveProperty('id');
        expect(res.body.firstname).toBe(payload.firstname);
    });

    it('DELETE /customers/:id should delete an existing customer', async () => {
        // GIVEN
        const createRes = await request(app)
            .post('/api/v1/customers')
            .send({
                name: 'Delete',
                firstname: 'Me',
                phoneNumber: '321',
                emailAddress: 'delete@test.com'
            });

        const id = createRes.body.id;

        // WHEN
        const res = await request(app)
            .delete(`/api/v1/customers/${id}`);

        // THEN
        expect(res.statusCode).toBe(204);
    });

    it('DELETE /customers/:id should return 404 if customer does not exist', async () => {
        const res = await request(app)
            .delete('/api/v1/customers/999997');

        expect(res.statusCode).toBe(404);
    });
});
