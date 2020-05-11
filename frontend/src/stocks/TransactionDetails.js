import React from 'react';
import Table from '@material-ui/core/Table';
import TableBody from '@material-ui/core/TableBody';
import TableCell from '@material-ui/core/TableCell';
import TableRow from '@material-ui/core/TableRow';

export default function TransactionDetails({transaction}) {
    return (
        <React.Fragment>
            <Table size="small">
                <TableBody>
                    <TableRow>
                        <TableCell>Type</TableCell>
                        <TableCell>{transaction.transactionType}</TableCell>
                    </TableRow>
                    <TableRow>
                        <TableCell>Status</TableCell>
                        <TableCell>{transaction.transactionStatus}</TableCell>
                    </TableRow>
                    <TableRow>
                        <TableCell>Reference</TableCell>
                        <TableCell>{transaction.reference}</TableCell>
                    </TableRow>
                    <TableRow>
                        <TableCell>Quantity</TableCell>
                        <TableCell>{transaction.quantity}</TableCell>
                    </TableRow>
                    <TableRow>
                        <TableCell>Current Price</TableCell>
                        <TableCell>{transaction.currentPrice}</TableCell>
                    </TableRow>
                    <TableRow>
                        <TableCell>Total</TableCell>
                        <TableCell>{transaction.totalAmount}</TableCell>
                    </TableRow>
                </TableBody>
            </Table>
        </React.Fragment>
    );
}