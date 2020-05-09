import {Snackbar} from '@material-ui/core';
import Alert from '@material-ui/lab/Alert';
import React from "react";

function notify(severity, message) {
    return <Snackbar autoHideDuration={6000} anchorOrigin={{vertical: 'top', horizontal: 'right'}} open={true} message={message}>
        <Alert severity={severity}> {message}  </Alert>
    </Snackbar>;
}

export const notifySuccess = message => notify('success', message);
export const notifyError = message => notify('error', message);
