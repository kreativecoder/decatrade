import DialogTitle from "@material-ui/core/DialogTitle";
import DialogContent from "@material-ui/core/DialogContent";
import DialogContentText from "@material-ui/core/DialogContentText";
import DialogActions from "@material-ui/core/DialogActions";
import Button from "@material-ui/core/Button";
import Dialog from "@material-ui/core/Dialog";
import React from "react";
import TransactionDetails from "./TransactionDetails";


export default function Complete(props) {
    const {openCompleteTransaction, handleClose, transaction, handleCancelTransaction, handleConfirmTransaction} = props;


    return (
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
                    <TransactionDetails transaction={transaction}/>
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
    );
}