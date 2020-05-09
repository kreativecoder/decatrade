const serverPort = process.env.PORT
export const API_BASE_URL = serverPort ? `http://localhost:${serverPort}/api/v1` : process.env.API_BASE_URL || 'http://localhost:5000/api/v1';
export const ACCESS_TOKEN = 'access_token';
