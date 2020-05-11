import React, {useEffect, useState} from 'react';
import clsx from 'clsx';
import {makeStyles} from '@material-ui/core/styles';
import Container from '@material-ui/core/Container';
import Grid from '@material-ui/core/Grid';
import Paper from '@material-ui/core/Paper';
import Chart from './Chart';
import Portfolio from './Portfolio';
import RecentTransactions from './RecentTransactions';
import Footer from "../common/Footer";
import Header from "../common/Header";
import {getUserPortfolioSummary} from "../decaTradeService";


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
}));

export default function Dashboard(props) {
    const classes = useStyles();
    const fixedHeightPaper = clsx(classes.paper, classes.fixedHeight);
    const [summary, setSummary] = useState({});

    useEffect(() => {
        loadSummary()
    }, []);

    const loadSummary = () => {
        getUserPortfolioSummary()
            .then(res => {
                console.log(res.data)
                setSummary(res.data)
            })
            .catch(function (error) {
                console.log(error.message)
                // notifyError(error.message || 'Sorry! Something went wrong. Please try again!');
            });
    }

    return (
        <div className={classes.root}>
            <Header pageTitle={'Dashboard'}/>
            <main className={classes.content}>
                <div className={classes.appBarSpacer}/>
                <Container maxWidth="lg" className={classes.container}>
                    <Grid container spacing={3}>
                        {/* Chart */}
                        <Grid item xs={12} md={8} lg={9}>
                            <Paper className={fixedHeightPaper}>
                                <Chart/>
                            </Paper>
                        </Grid>
                        {/* Recent Portfolio */}
                        <Grid item xs={12} md={4} lg={3}>
                            <Paper className={fixedHeightPaper}>
                                <Portfolio value={summary.portfolioValue}/>
                            </Paper>
                        </Grid>
                        {/* Recent RecentTransactions */}
                        <Grid item xs={12}>
                            <Paper className={classes.paper}>
                                <RecentTransactions transactions={summary.recentTransactions}/>
                            </Paper>
                        </Grid>
                    </Grid>
                    <Footer/>
                </Container>
            </main>
        </div>
    );
}