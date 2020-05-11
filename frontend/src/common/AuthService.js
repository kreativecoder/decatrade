import {ACCESS_TOKEN} from "../constants";
import jwt_decode from "jwt-decode"

export const isAuthenticated = () => {
    const token = localStorage.getItem(ACCESS_TOKEN);
    if (!token) return false

    const jwt = jwt_decode(token)
    const current_time = Date.now() / 1000;
    return jwt.exp > current_time;
}