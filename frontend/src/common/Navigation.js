import List from "@material-ui/core/List";
import ListItem from "@material-ui/core/ListItem";
import {Link} from "react-router-dom";
import ListItemIcon from "@material-ui/core/ListItemIcon";
import DashboardIcon from "@material-ui/icons/Dashboard";
import ListItemText from "@material-ui/core/ListItemText";
import ShoppingCartIcon from "@material-ui/icons/ShoppingCart";
import BarChartIcon from "@material-ui/icons/BarChart";
import React from "react";


export default function Navigation({title}) {
    return <List>
        <div>
            <ListItem button selected={title === 'Dashboard'} component={Link} to="/dashboard">
                <ListItemIcon>
                    <DashboardIcon/>
                </ListItemIcon>
                <ListItemText primary="Dashboard"/>
            </ListItem>
            <ListItem button selected={title === 'Stocks'} component={Link} to="/stocks">
                <ListItemIcon>
                    <ShoppingCartIcon/>
                </ListItemIcon>
                <ListItemText primary="Stocks"/>
            </ListItem>
            <ListItem button selected={title === 'Transactions'} component={Link} to="/transactions">
                <ListItemIcon>
                    <BarChartIcon/>
                </ListItemIcon>
                <ListItemText primary="Transactions"/>
            </ListItem>
        </div>
    </List>;
}