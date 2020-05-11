import React from 'react';
import {Redirect, Route} from "react-router-dom";
import {isAuthenticated} from "./AuthService";


const PrivateRoute = ({component: Component, authenticated, ...rest}) => {
    console.log("Authenticated: " + authenticated)
    console.log("Current User " + rest.currentUser)
    return (
    <Route
        {...rest}
        render={props =>
            isAuthenticated() ? (
                <Component {...rest} {...props} />
            ) : (
                <Redirect
                    to={{
                        pathname: '/login',
                        state: {from: props.location}
                    }}
                />
            )
        }
    />
);}

export default PrivateRoute