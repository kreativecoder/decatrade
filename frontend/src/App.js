import React from "react";
import {BrowserRouter as Router, Route, Switch} from "react-router-dom";
import Login from "./user/login/Login";
import SignUp from "./user/signup/SignUp";
import Dashboard from "./dashboard/Dashboard";
import Stocks from "./stocks/Stocks";
import Transactions from "./transactions/Transactions"

export default function App() {
    return (
        <Router>
            <Switch>
                <Route path="/login" component={Login}/>
                <Route path="/signup" component={SignUp}/>
                <Route path="/dashboard" component={Dashboard}/>
                <Route path="/stocks" component={Stocks}/>
                <Route path="/transactions" component={Transactions}/>
                <Route path="/" component={SignUp}/>
            </Switch>
        </Router>
    );
}