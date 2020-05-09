import axios from 'axios'

export function checkUsername(username) {
    return axios.get('http://localhost:5000/api/v1/users/check?username=' + username);
}

export function signup(payload) {
    return axios.post('http://localhost:5000/api/v1/users', payload);
}

export function login(payload) {
    return axios.post('http://localhost:5000/api/v1/users/login', payload);
}