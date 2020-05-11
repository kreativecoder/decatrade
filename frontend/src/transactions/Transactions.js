import React, {useEffect, useState} from 'react';
import {makeStyles} from '@material-ui/core/styles';
import Container from '@material-ui/core/Container';
import Footer from "../common/Footer";
import Paper from '@material-ui/core/Paper';
import Grid from "@material-ui/core/Grid";
import {getTransactions} from "../decaTradeService";
import Header from "../common/Header";
import TransactionTable from "./TransactionTable";

const useStyles = makeStyles((theme) => ({
    root: {
        display: 'flex',
    },
    appBarSpacer: theme.mixins.toolbar,
    content: {
        flexGrow: 1,
        height: '100vh',
        overflow: 'auto',
    },
    container: {
        paddingTop: theme.spacing(4),
        paddingBottom: theme.spacing(4),
    },
    paper: {
        padding: theme.spacing(2),
        display: 'flex',
        overflow: 'auto',
        flexDirection: 'column',
    },
    fixedHeight: {
        height: 240,
    },
    formControl: {
        margin: theme.spacing(1),
        minWidth: 70,
    },
}));

export default function Transactions(props) {
    const classes = useStyles();
    const [transactions, setTransactions] = useState([]);

    useEffect(() => {
        loadTransactions()
    }, []);

    const loadTransactions = () => {
        getTransactions()
            .then(res => {
                console.log(res.data)
                setTransactions(res.data)
            })
            .catch(function (error) {
                console.log(error.message)
                // notifyError(error.message || 'Sorry! Something went wrong. Please try again!');
            });
    }

    return (
        <div className={classes.root}>
            <Header pageTitle={'Transactions'}/>
            <main className={classes.content}>
                <div className={classes.appBarSpacer}/>
                <Container maxWidth="lg" className={classes.container}>
                    <Grid container spacing={3}>
                        <Grid item xs={12}>
                            <Paper className={classes.paper}>
                                <TransactionTable transactions={transactions}/>
                            </Paper>
                        </Grid>
                    </Grid>
                    <Footer/>
                </Container>
            </main>
        </div>
    );
}