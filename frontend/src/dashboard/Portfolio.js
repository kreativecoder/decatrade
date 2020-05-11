import React from 'react';
import Link from '@material-ui/core/Link';
import {makeStyles} from '@material-ui/core/styles';
import Typography from '@material-ui/core/Typography';
import Title from '../common/Title';

function preventDefault(event) {
    event.preventDefault();
}

const useStyles = makeStyles({
    depositContext: {
        flex: 1,
    },
});

export default function Portfolio({value}) {
    const classes = useStyles();
    return (
        <React.Fragment>
            <Title>Portfolio Value</Title>
            <Typography component="p" variant="h4">
                $ {value}
            </Typography>
            <Typography color="textSecondary" className={classes.depositContext}>
                **based on recent prices
            </Typography>
            <div>
                <Link color="primary" href="/stocks" onClick={preventDefault}>
                    View stocks
                </Link>
            </div>
        </React.Fragment>
    );
}