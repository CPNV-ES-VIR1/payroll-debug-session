# --- Base stage (dependency install) ---
FROM node:18-alpine AS base

WORKDIR /app

# Copy package files
COPY package*.json ./

# Install all dependencies (including dev for tests)
RUN npm install

# Copy source code
COPY . .


# --- Test stage ---
FROM base AS test

# Run your test script (must exist in package.json)
RUN npm test


# --- Production stage ---
FROM node:18-alpine AS production

WORKDIR /app

# Copy package.json files
COPY package*.json ./

# Install ONLY production dependencies
RUN npm install --omit=dev --verbose

# Copy only the needed application code (not dev files)
COPY . .

# SQLite persistence
VOLUME ["/data"]

# Expose your Express port
EXPOSE 3000

# Start the application
CMD ["npm", "start"]