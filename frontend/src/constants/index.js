const serverPort = process.env.PORT
console.log("SERVER_PORT: " + serverPort)
export const API_BASE_URL = serverPort ? `http://localhost:${serverPort}/api/v1` : process.env.API_BASE_URL || 'http://localhost:5000/api/v1';
console.log("API_BASE_URL: " + API_BASE_URL)
export const ACCESS_TOKEN = 'access_token';
