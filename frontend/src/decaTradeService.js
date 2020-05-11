import axios from 'axios'
import {API_BASE_PATH} from "./constants";

export function checkUsername(username) {
    return axios.get(API_BASE_PATH + '/users/check?username=' + username);
}

export function signup(payload) {
    return axios.post(API_BASE_PATH + '/users', payload);
}

export function login(payload) {
    return axios.post(API_BASE_PATH + '/users/login', payload);
}

export function getStocks() {
    setAuthToken()
    return axios.get(API_BASE_PATH + '/stocks');
}

export function getAllSymbols() {
    return axios.get(API_BASE_PATH + '/stocks/symbols');
}

export function initiateTransaction(payload) {
    setAuthToken()
    return axios.post(API_BASE_PATH + '/transactions', payload);
}

export function confirmTransaction(reference) {
    setAuthToken()
    return axios.get(API_BASE_PATH + '/transactions/confirm?reference=' + reference);
}

export function cancelTransaction(reference) {
    setAuthToken()
    return axios.delete(API_BASE_PATH + '/transactions/' + reference);
}

export function getTransactions() {
    setAuthToken()
    return axios.get(API_BASE_PATH + '/transactions');
}

export function getCurrentUser() {
    setAuthToken()
    return axios.get(API_BASE_PATH + '/users/current');
}

export function getUserPortfolioSummary() {
    setAuthToken()
    return axios.get(API_BASE_PATH + '/users/portfolio/summary');
}

export function setAuthToken() {
    const token = localStorage.getItem('access_token');
    axios.defaults.headers.common['Authorization'] = '';
    delete axios.defaults.headers.common['Authorization'];

    if (token) {
        axios.defaults.headers.common['Authorization'] = `Bearer ${token}`;
    }
}