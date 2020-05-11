import React, {useEffect, useState} from 'react';
import {makeStyles} from '@material-ui/core/styles';
import Container from '@material-ui/core/Container';
import Footer from "../common/Footer";
import {Table, TableBody, TableCell, TableHead, TableRow} from '@material-ui/core';
import Paper from '@material-ui/core/Paper';
import Grid from "@material-ui/core/Grid";
import {getTransactions} from "../decaTradeService";
import Header from "../common/Header";

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
                                <Table className={classes.table} aria-label="simple table">
                                    <TableHead>
                                        <TableRow>
                                            <TableCell>Symbol</TableCell>
                                            <TableCell>Quantity</TableCell>
                                            <TableCell>Amount</TableCell>
                                            <TableCell>Type</TableCell>
                                            <TableCell>Status</TableCell>
                                            <TableCell>reference</TableCell>
                                            <TableCell>Date</TableCell>
                                        </TableRow>
                                    </TableHead>
                                    <TableBody>
                                        {transactions.map((transaction) => (
                                            <TableRow key={transaction.reference}>
                                                <TableCell>{transaction.symbol}</TableCell>
                                                <TableCell>{transaction.quantity}</TableCell>
                                                <TableCell>{transaction.totalAmount}</TableCell>
                                                <TableCell>{transaction.transactionType}</TableCell>
                                                <TableCell>{transaction.transactionStatus}</TableCell>
                                                <TableCell>{transaction.reference}</TableCell>
                                                <TableCell>{transaction.transactionDate}</TableCell>
                                            </TableRow>
                                        ))}
                                    </TableBody>
                                </Table>
                            </Paper>
                        </Grid>
                    </Grid>
                    <Footer/>
                </Container>
            </main>
        </div>
    );
}