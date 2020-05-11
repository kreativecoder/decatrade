import React, {useEffect, useState} from "react";
import {BrowserRouter as Router, Route, Switch} from "react-router-dom";
import Login from "./user/login/Login";
import SignUp from "./user/signup/SignUp";
import Dashboard from "./dashboard/Dashboard";
import Stocks from "./stocks/Stocks";
import Transactions from "./transactions/Transactions"
import {getCurrentUser} from "./decaTradeService";
import PrivateRoute from "./common/PrivateRoute";
import {ACCESS_TOKEN} from "./constants";

export default function App(props) {
    // const [loading, setLoading] = useState(false);
    const [currentUser, setCurrentUser] = useState(null);

    useEffect(() => {
        loadCurrentUser()
    }, []);

    const loadCurrentUser = () => {
        getCurrentUser()
            .then(response => {
                // setLoading(false)
                setCurrentUser(response.data)
            })
            .catch(error => {
                console.log(error.message)
                // setLoading(false);
            });
    }

    const handleLogout = (redirectTo = "/", notificationType = "success", description = "You're successfully logged out.") => {
        localStorage.removeItem(ACCESS_TOKEN);

        setCurrentUser(null)
        this.props.history.push(redirectTo);

        // notification[notificationType]({
        //     message: 'Polling App',
        //     description: description,
        // });
    }

    return (
        <Router>
            <Switch>
                <Route exact path="/login" render={(props) => <Login postLogin={loadCurrentUser} {...props} />}/>
                <Route path="/signup" component={SignUp}/>
                <PrivateRoute  path="/dashboard" currentUser={currentUser}
                              logout={handleLogout} component={Dashboard}/>
                <PrivateRoute exact path="/" currentUser={currentUser}
                              logout={handleLogout} component={Dashboard}/>
                <PrivateRoute  path="/stocks" currentUser={currentUser}
                              logout={handleLogout} component={Stocks}/>
                <PrivateRoute  path="/transactions" currentUser={currentUser}
                              logout={handleLogout} component={Transactions}/>
            </Switch>
        </Router>
    );
}