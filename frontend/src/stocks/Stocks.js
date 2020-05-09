import React, {useEffect, useState} from 'react';
import clsx from 'clsx';
import {makeStyles} from '@material-ui/core/styles';
import CssBaseline from '@material-ui/core/CssBaseline';
import Drawer from '@material-ui/core/Drawer';
import AppBar from '@material-ui/core/AppBar';
import Toolbar from '@material-ui/core/Toolbar';
import Typography from '@material-ui/core/Typography';
import Divider from '@material-ui/core/Divider';
import IconButton from '@material-ui/core/IconButton';
import Badge from '@material-ui/core/Badge';
import Container from '@material-ui/core/Container';
import MenuIcon from '@material-ui/icons/Menu';
import ChevronLeftIcon from '@material-ui/icons/ChevronLeft';
import NotificationsIcon from '@material-ui/icons/Notifications';
import Footer from "../common/Footer";
import Navigation from "../common/Navigation";
import {Table, TableBody, TableCell, TableHead, TableRow} from '@material-ui/core';
import Paper from '@material-ui/core/Paper';
import Grid from "@material-ui/core/Grid";
import Button from '@material-ui/core/Button';
import DialogTitle from "@material-ui/core/DialogTitle";
import DialogContent from "@material-ui/core/DialogContent";
import DialogContentText from "@material-ui/core/DialogContentText";
import TextField from "@material-ui/core/TextField";
import DialogActions from "@material-ui/core/DialogActions";
import Dialog from "@material-ui/core/Dialog";
import InputLabel from '@material-ui/core/InputLabel';
import Input from '@material-ui/core/Input';
import FormControl from '@material-ui/core/FormControl';
import Select from '@material-ui/core/Select';
import {
    cancelTransaction,
    confirmTransaction,
    getAllSymbols,
    getStocks,
    initiateTransaction
} from "../decaTradeService";

const drawerWidth = 240;

const useStyles = makeStyles((theme) => ({
    root: {
        display: 'flex',
    },
    toolbar: {
        paddingRight: 24, // keep right padding when drawer closed
    },
    toolbarIcon: {
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'flex-end',
        padding: '0 8px',
        ...theme.mixins.toolbar,
    },
    appBar: {
        zIndex: theme.zIndex.drawer + 1,
        transition: theme.transitions.create(['width', 'margin'], {
            easing: theme.transitions.easing.sharp,
            duration: theme.transitions.duration.leavingScreen,
        }),
    },
    appBarShift: {
        marginLeft: drawerWidth,
        width: `calc(100% - ${drawerWidth}px)`,
        transition: theme.transitions.create(['width', 'margin'], {
            easing: theme.transitions.easing.sharp,
            duration: theme.transitions.duration.enteringScreen,
        }),
    },
    menuButton: {
        marginRight: 36,
    },
    menuButtonHidden: {
        display: 'none',
    },
    title: {
        flexGrow: 1,
    },
    drawerPaper: {
        position: 'relative',
        whiteSpace: 'nowrap',
        width: drawerWidth,
        transition: theme.transitions.create('width', {
            easing: theme.transitions.easing.sharp,
            duration: theme.transitions.duration.enteringScreen,
        }),
    },
    drawerPaperClose: {
        overflowX: 'hidden',
        transition: theme.transitions.create('width', {
            easing: theme.transitions.easing.sharp,
            duration: theme.transitions.duration.leavingScreen,
        }),
        width: theme.spacing(7),
        [theme.breakpoints.up('sm')]: {
            width: theme.spacing(9),
        },
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

export default function Stocks() {
    const classes = useStyles();
    const [open, setOpen] = useState(true);
    const [symbol, setSymbol] = useState('');
    const [quantity, setQuantity] = useState('');
    const [allSymbols, setAllSymbols] = useState([]);
    const [stocks, setStocks] = useState([]);
    const [openBuyStock, setOpenBuyStock] = useState(false);
    const [openCompleteTransaction, setOpenCompleteTransaction] = useState(false);
    const [transactionRes, setTransactionRes] = useState({});

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

    const handleChange = (event) => {
        //get symbol
        setSymbol(event.target.value || '');
    };

    const handleDrawerOpen = () => {
        setOpen(true);
    };
    const handleDrawerClose = () => {
        setOpen(false);
    };

    const handleConfirmTransaction = () => {
        confirmTransaction(transactionRes.reference)
            .then(res => {
                console.log(res.data)
                // notifySuccess('Transaction Successful!')
                setOpenCompleteTransaction(false)
                loadStocks()
            })
            .catch(function (error) {
                console.log(error.message)
                // notifyError(error.message || 'Sorry! Something went wrong. Please try again!');
            });
    };

    const handleCancelTransaction = () => {
        cancelTransaction(transactionRes.reference)
            .then(res => {
                console.log(res.data)
                // notifySuccess('Transaction Cancelled!')
                setOpenCompleteTransaction(false)
                loadStocks()
            })
            .catch(function (error) {
                console.log(error.message)
                // notifyError(error.message || 'Sorry! Something went wrong. Please try again!');
            });
    };

    const handleBuy = () => {
        const payload = {
            "symbol": symbol,
            "quantity": quantity,
            "transactionType": "BUY",
            "reference": "UI_" + Date.now()
        }
        initiateTransaction(payload)
            .then(res => {
                console.log(res.data)
                setTransactionRes(res.data)
                setOpenBuyStock(false);
                setOpenCompleteTransaction(true)
            })
            .catch(function (error) {
                console.log(error.message)
                // notifyError(error.message || 'Sorry! Something went wrong. Please try again!');
            });
    };

    const handleClickOpen = () => {
        getAllSymbols()
            .then(res => {
                console.log(res.data)
                setAllSymbols(res.data)
                setSymbol(res.data[0].symbol);
                setOpenBuyStock(true);
            })
            .catch(function (error) {
                // notifyError(error.message || 'Sorry! Something went wrong. Please try again!');
            });

    };

    const handleClose = () => {
        setOpenBuyStock(false);
    };

    return (
        <div className={classes.root}>
            <CssBaseline/>
            <AppBar position="absolute" className={clsx(classes.appBar, open && classes.appBarShift)}>
                <Toolbar className={classes.toolbar}>
                    <IconButton
                        edge="start"
                        color="inherit"
                        aria-label="open drawer"
                        onClick={handleDrawerOpen}
                        className={clsx(classes.menuButton, open && classes.menuButtonHidden)}
                    >
                        <MenuIcon/>
                    </IconButton>
                    <Typography component="h1" variant="h6" color="inherit" noWrap className={classes.title}>
                        Stocks
                    </Typography>
                    <IconButton color="inherit">
                        <Badge badgeContent={4} color="secondary">
                            <NotificationsIcon/>
                        </Badge>
                    </IconButton>
                </Toolbar>
            </AppBar>
            <Drawer
                variant="permanent"
                classes={{
                    paper: clsx(classes.drawerPaper, !open && classes.drawerPaperClose),
                }}
                open={open}
            >
                <div className={classes.toolbarIcon}>
                    <IconButton onClick={handleDrawerClose}>
                        <ChevronLeftIcon/>
                    </IconButton>
                </div>
                <Divider/>
                <Navigation/>
            </Drawer>
            <main className={classes.content}>
                <div className={classes.appBarSpacer}/>
                <Container maxWidth="lg" className={classes.container}>
                    <Grid container spacing={3}>
                        <Grid item xs={2}>
                            <Paper className={classes.paper}>
                                <Button variant="contained" color="primary" onClick={handleClickOpen}>
                                    Buy Stock
                                </Button>
                                <Dialog disableBackdropClick={true} disableEscapeKeyDown={true} open={openBuyStock}
                                        onClose={handleClose} aria-labelledby="form-dialog-title">
                                    <DialogTitle id="form-dialog-title">Buy Stock</DialogTitle>
                                    <DialogContent>
                                        <DialogContentText>
                                            Stock purchase order
                                        </DialogContentText>
                                        <form className={classes.container}>
                                            <FormControl variant="outlined" className={classes.formControl}>
                                                <InputLabel htmlFor="demo-dialog-native">Company</InputLabel>
                                                <Select
                                                    native
                                                    value={symbol}
                                                    onChange={handleChange}
                                                    input={<Input id="demo-dialog-native"/>}
                                                >
                                                    {allSymbols.map(s => <option key={s.symbol}
                                                                                 value={s.symbol}>{s.name}</option>)}
                                                </Select>
                                            </FormControl>
                                            <TextField
                                                autoFocus
                                                variant="outlined"
                                                margin="normal"
                                                required
                                                id="symbol"
                                                name="symbol"
                                                label="Symbol"
                                                value={symbol}
                                                fullWidth
                                            />
                                            <TextField
                                                autoFocus
                                                variant="outlined"
                                                margin="normal"
                                                required
                                                id="quantity"
                                                name="quantity"
                                                label="Quantity"
                                                type="number"
                                                fullWidth
                                                onChange={e => setQuantity(e.target.value)}
                                            />
                                        </form>

                                    </DialogContent>
                                    <DialogActions>
                                        <Button onClick={handleClose} color="primary">
                                            Cancel
                                        </Button>
                                        <Button onClick={handleBuy} color="primary">
                                            Buy
                                        </Button>
                                    </DialogActions>
                                </Dialog>
                                <Dialog
                                    disableBackdropClick={true}
                                    disableEscapeKeyDown={true}
                                    open={openCompleteTransaction}
                                    onClose={handleClose}
                                    aria-labelledby="alert-dialog-title"
                                    aria-describedby="alert-dialog-description"
                                >
                                    <DialogTitle id="alert-dialog-title">{"Complete Transaction"}</DialogTitle>
                                    <DialogContent>
                                        <DialogContentText id="alert-dialog-description">
                                            The total cost of this transaction is {transactionRes.totalAmount}, proceed?
                                        </DialogContentText>
                                    </DialogContent>
                                    <DialogActions>
                                        <Button onClick={handleCancelTransaction} color="secondary">
                                            Cancel
                                        </Button>
                                        <Button onClick={handleConfirmTransaction} color="primary" autoFocus>
                                            Confirm
                                        </Button>
                                    </DialogActions>
                                </Dialog>
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
                                                <TableCell><Button size="small" variant="contained" color="primary">
                                                    BUY
                                                </Button>
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