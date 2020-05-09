import React from "react";
import {BrowserRouter as Router, Route, Switch} from "react-router-dom";
import Login from "./user/login/Login";
import SignUp from "./user/signup/SignUp";
import Dashboard from "./dashboard/Dashboard";

export default function App() {
    return (
        <Router>
            <Switch>
                <Route path="/login" component={Login}/>
                <Route path="/signup" component={SignUp}/>
                <Route path="/dashboard" component={Dashboard}/>
                <Route path="/" component={SignUp}/>
            </Switch>
        </Router>

    );
}