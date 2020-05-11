import React from 'react';
import Link from '@material-ui/core/Link';
import {makeStyles} from '@material-ui/core/styles';
import Title from '../common/Title';
import TransactionTable from "../transactions/TransactionTable";

const useStyles = makeStyles((theme) => ({
    seeMore: {
        marginTop: theme.spacing(3),
    },
}));

export default function RecentTransactions({transactions}) {
    const classes = useStyles();
    return (
        <React.Fragment>
            <Title>Recent Transactions</Title>
            <TransactionTable transactions={transactions}/>
            {
                transactions && transactions.size > 5 && <div className={classes.seeMore}>
                    <Link color="primary" href="/transactions">
                        See more transactions
                    </Link>
                </div>
            }

        </React.Fragment>
    );
}