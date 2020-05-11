import {Table, TableBody, TableCell, TableHead, TableRow} from "@material-ui/core";
import React from "react";
import {makeStyles} from "@material-ui/core/styles";

const useStyles = makeStyles((theme) => ({
    root: {
        display: 'flex',
    },
}));

export default function TransactionTable({transactions}) {
    const classes = useStyles();

    return (
        <Table className={classes.table} aria-label="simple table">
            <TableHead>
                <TableRow>
                    <TableCell>Symbol</TableCell>
                    <TableCell>Quantity</TableCell>
                    <TableCell>Amount</TableCell>
                    <TableCell>Type</TableCell>
                    <TableCell>Status</TableCell>
                    <TableCell>Reference</TableCell>
                    <TableCell>Date</TableCell>
                </TableRow>
            </TableHead>
            <TableBody>
                {transactions && transactions.map((transaction) => (
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
    );
}