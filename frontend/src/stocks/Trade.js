import DialogTitle from "@material-ui/core/DialogTitle";
import DialogContent from "@material-ui/core/DialogContent";
import DialogActions from "@material-ui/core/DialogActions";
import Button from "@material-ui/core/Button";
import Dialog from "@material-ui/core/Dialog";
import React from "react";
import FormControl from "@material-ui/core/FormControl";
import InputLabel from "@material-ui/core/InputLabel";
import Select from "@material-ui/core/Select";
import Input from "@material-ui/core/Input";
import TextField from "@material-ui/core/TextField";
import {makeStyles} from "@material-ui/core/styles";

const useStyles = makeStyles((theme) => ({
    container: {
        paddingTop: theme.spacing(4),
        paddingBottom: theme.spacing(4),
    },
    formControl: {
        margin: theme.spacing(1),
        minWidth: 70,
    },
}));

export default function Trade(props) {
    const classes = useStyles();
    const {
        openTradeStock, handleClose, allSymbols, setSymbol, setQuantity, handleInitiateTransaction,
        symbol, tradeType, quantity
    } = props;

    console.log(tradeType)

    return (
        <Dialog disableBackdropClick={true} disableEscapeKeyDown={true} open={openTradeStock}
                onClose={handleClose} aria-labelledby="form-dialog-title">
            <DialogTitle id="form-dialog-title">{tradeType === 1 ? 'Sell' : 'Buy'} Stock</DialogTitle>
            <DialogContent>
                <form className={classes.container}>
                    {tradeType === 2 && <FormControl variant="outlined" className={classes.formControl}>
                        <InputLabel htmlFor="demo-dialog-native">Company</InputLabel>
                        <Select
                            native
                            value={symbol}
                            onChange={e => setSymbol(e.target.value)}
                            input={<Input id="demo-dialog-native"/>}
                        >
                            {allSymbols.map(s => <option key={s.symbol}
                                                         value={s.symbol}>{s.name}</option>)}
                        </Select>
                    </FormControl>}
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
                        value={quantity}
                    />
                </form>

            </DialogContent>
            <DialogActions>
                <Button onClick={handleClose} color="primary">
                    Cancel
                </Button>
                <Button onClick={handleInitiateTransaction} color="primary">
                    {tradeType === 1 ? 'Sell' : 'Buy'}
                </Button>
            </DialogActions>
        </Dialog>
    );
}