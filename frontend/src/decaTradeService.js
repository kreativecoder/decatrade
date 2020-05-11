import axios from 'axios'
import {API_BASE_URL} from "./constants";

export function checkUsername(username) {
    return axios.get(API_BASE_URL + '/users/check?username=' + username);
}

export function signup(payload) {
    return axios.post(API_BASE_URL + '/users', payload);
}

export function login(payload) {
    return axios.post(API_BASE_URL + '/users/login', payload);
}

export function getStocks() {
    setAuthToken()
    return axios.get(API_BASE_URL + '/stocks');
}

export function getAllSymbols() {
    return axios.get(API_BASE_URL + '/stocks/symbols');
}

export function initiateTransaction(payload) {
    setAuthToken()
    return axios.post(API_BASE_URL + '/transactions', payload);
}

export function confirmTransaction(reference) {
    setAuthToken()
    return axios.get(API_BASE_URL + '/transactions/confirm?reference=' + reference);
}

export function cancelTransaction(reference) {
    setAuthToken()
    return axios.delete(API_BASE_URL + '/transactions/' + reference);
}

export function getTransactions() {
    setAuthToken()
    return axios.get(API_BASE_URL + '/transactions');
}

export function getCurrentUser() {
    setAuthToken()
    return axios.get(API_BASE_URL + '/users/current');
}

export function getUserPortfolioSummary() {
    setAuthToken()
    return axios.get(API_BASE_URL + '/users/portfolio/summary');
}

export function setAuthToken() {
    const token = localStorage.getItem('access_token');
    axios.defaults.headers.common['Authorization'] = '';
    delete axios.defaults.headers.common['Authorization'];

    if (token) {
        axios.defaults.headers.common['Authorization'] = `Bearer ${token}`;
    }
}