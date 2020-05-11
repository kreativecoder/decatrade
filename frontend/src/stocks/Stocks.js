import React, {useEffect, useState} from 'react';
import {makeStyles} from '@material-ui/core/styles';
import CssBaseline from '@material-ui/core/CssBaseline';
import Container from '@material-ui/core/Container';
import Footer from "../common/Footer";
import {Table, TableBody, TableCell, TableHead, TableRow} from '@material-ui/core';
import Paper from '@material-ui/core/Paper';
import Grid from "@material-ui/core/Grid";
import Button from '@material-ui/core/Button';
import {
    cancelTransaction,
    confirmTransaction,
    getAllSymbols,
    getStocks,
    initiateTransaction
} from "../decaTradeService";
import Trade from "./Trade";
import Complete from "./Complete";
import Header from "../common/Header";
import {useSnackbar} from "notistack";

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

export default function Stocks(props) {
    const classes = useStyles();
    const [symbol, setSymbol] = useState('');
    const [quantity, setQuantity] = useState(0);
    const [allSymbols, setAllSymbols] = useState([]);
    const [stocks, setStocks] = useState([]);
    const [openTrade, setOpenTrade] = useState(false);
    const [openCompleteTransaction, setOpenCompleteTransaction] = useState(false);
    const [transactionRes, setTransactionRes] = useState({});
    const [tradeType, setTradeTye] = useState(2);
    const {enqueueSnackbar} = useSnackbar();

    useEffect(() => {
        loadStocks()
    }, []);

    const loadStocks = () => {
        setTransactionRes({})

        getStocks()
            .then(res => {
                console.log(res.data)
                setStocks(res.data)
            })
            .catch(function (error) {
                console.log(error.message)
                // notifyError(error.message || 'Sorry! Something went wrong. Please try again!');
            });
    }

    const handleConfirmTransaction = () => {
        confirmTransaction(transactionRes.reference)
            .then(res => {
                enqueueSnackbar(`Transaction ${res.data.reference} successful.`, {variant: 'success'});
                setOpenCompleteTransaction(false)
                loadStocks()
            })
            .catch(function (error) {
                if (error.response) {
                    enqueueSnackbar(error.response.data.message, {variant: 'error'});
                } else {
                    enqueueSnackbar(error.message || 'Sorry! Something went wrong. Please try again!', {variant: 'error'});
                }
            });
    };

    const handleCancelTransaction = () => {
        cancelTransaction(transactionRes.reference)
            .then(res => {
                enqueueSnackbar(`Transaction Cancelled!`, {variant: 'info'});
                setOpenCompleteTransaction(false)
                loadStocks()
            })
            .catch(function (error) {
                if (error.response) {
                    enqueueSnackbar(error.response.data.message, {variant: 'error'});
                } else {
                    enqueueSnackbar(error.message || 'Sorry! Something went wrong. Please try again!', {variant: 'error'});
                }
            });
    };

    const handleInitiateTransaction = () => {
        const tranType = tradeType === 1 ? 'SELL' : 'BUY';
        const payload = {
            "symbol": symbol,
            "quantity": quantity,
            "transactionType": tranType,
            "reference": `Deca_${tranType}_${Date.now()}`
        }
        initiateTransaction(payload)
            .then(res => {
                setTransactionRes(res.data)
                setOpenTrade(false);
                setOpenCompleteTransaction(true)
            })
            .catch(function (error) {
                if (error.response) {
                    enqueueSnackbar(error.response.data.message, {variant: 'error'});
                } else {
                    enqueueSnackbar(error.message || 'Sorry! Something went wrong. Please try again!', {variant: 'error'});
                }
            });
    };

    const handleClickBuy = (type) => {
        if (type === 3) {
            setTradeTye(type)
            setOpenTrade(true);
        } else {
            getAllSymbols()
                .then(res => {
                    console.log(res.data)
                    setAllSymbols(res.data)
                    setSymbol(res.data[0].symbol);
                    setTradeTye(type)
                    setOpenTrade(true);
                })
                .catch(function (error) {
                    enqueueSnackbar(error.message || 'Sorry! Something went wrong. Please try again!', {variant: 'error'});
                });
        }
    };

    const handleClickSell = (tradeType, quantity) => {
        setTradeTye(tradeType)
        setQuantity(quantity)
        setOpenTrade(true);
    };

    const handleClick = (event, stock, action) => {
        setSymbol(stock.symbol);
        if (action === 3) {
            handleClickBuy(action)
        } else {
            handleClickSell(action, stock.quantity)
        }
    };

    const handleClose = () => {
        setOpenTrade(false);
    };

    return (
        <div className={classes.root}>
            <CssBaseline/>
            <Header pageTitle={'Stocks'}/>
            <main className={classes.content}>
                <div className={classes.appBarSpacer}/>
                <Container maxWidth="lg" className={classes.container}>
                    <Grid container spacing={3}>
                        <Grid item xs={2}>
                            <Paper className={classes.paper}>
                                <Button variant="contained" color="primary" onClick={() => handleClickBuy(2)}>
                                    Buy Stock
                                </Button>
                                <Trade openTradeStock={openTrade} setSymbol={setSymbol}
                                       handleClose={handleClose}
                                       setQuantity={setQuantity}
                                       allSymbols={allSymbols}
                                       symbol={symbol}
                                       handleInitiateTransaction={handleInitiateTransaction}
                                       tradeType={tradeType}
                                       quantity={quantity}/>

                                <Complete openCompleteTransaction={openCompleteTransaction}
                                          transaction={transactionRes}
                                          handleCancelTransaction={handleCancelTransaction}
                                          handleConfirmTransaction={handleConfirmTransaction}
                                />
                            </Paper>
                        </Grid>

                        <Grid item xs={12}>
                            <Paper className={classes.paper}>
                                <Table className={classes.table} aria-label="simple table">
                                    <TableHead>
                                        <TableRow>
                                            <TableCell>Symbol</TableCell>
                                            <TableCell>Quantity</TableCell>
                                            <TableCell>Last Price</TableCell>
                                            <TableCell>Current Value</TableCell>
                                            <TableCell>Amount Paid</TableCell>
                                            <TableCell>% Gain/Loss</TableCell>
                                            <TableCell>Actions</TableCell>
                                        </TableRow>
                                    </TableHead>
                                    <TableBody>
                                        {stocks.map((stock) => (
                                            <TableRow key={stock.symbol}>
                                                <TableCell component="th" scope="row">
                                                    {stock.symbol}
                                                </TableCell>
                                                <TableCell>{stock.quantity}</TableCell>
                                                <TableCell>{stock.lastPrice}</TableCell>
                                                <TableCell>{stock.currentValue}</TableCell>
                                                <TableCell>{stock.amountPaid}</TableCell>
                                                <TableCell>{stock.percentageChange}</TableCell>
                                                <TableCell align="right"
                                                           onClick={event => handleClick(event, stock, 3)}><Button
                                                    size="small" variant="contained" color="primary">
                                                    BUY
                                                </Button>
                                                </TableCell>
                                                <TableCell
                                                    onClick={event => handleClick(event, stock, 1)}>
                                                    <Button size="small" variant="contained" color="secondary">
                                                        SELL
                                                    </Button>
                                                </TableCell>
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