import React, {useState} from 'react';
import Avatar from '@material-ui/core/Avatar';
import CssBaseline from '@material-ui/core/CssBaseline';
import TextField from '@material-ui/core/TextField';
import Checkbox from '@material-ui/core/Checkbox';
import Link from '@material-ui/core/Link';
import LockOutlinedIcon from '@material-ui/icons/LockOutlined';
import Typography from '@material-ui/core/Typography';
import {makeStyles} from '@material-ui/core/styles';
import Container from '@material-ui/core/Container';
import {Button, CircularProgress, FormControlLabel, Grid} from "@material-ui/core";
import {login} from "../../decaTradeService";
import {ACCESS_TOKEN} from '../../constants';
import {useHistory} from "react-router-dom";
import {isAuthenticated} from "../../common/AuthService";
import {useSnackbar} from "notistack";
import Footer from "../../common/Footer";

const useStyles = makeStyles((theme) => ({
    paper: {
        marginTop: theme.spacing(8),
        display: 'flex',
        flexDirection: 'column',
        alignItems: 'center',
    },
    avatar: {
        margin: theme.spacing(1),
        backgroundColor: theme.palette.secondary.main,
    },
    form: {
        width: '100%', // Fix IE 11 issue.
        marginTop: theme.spacing(1),
    },
    submit: {
        margin: theme.spacing(3, 0, 2),
    },
}));

export default function Login(props) {

    if (isAuthenticated()) {
        props.history.push("/")
    }

    const classes = useStyles();
    const {enqueueSnackbar} = useSnackbar();
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    const [loading, setLoading] = useState(false);
    let history = useHistory();

    function handleSubmit(e) {
        e.preventDefault()
        const payload = {
            "username": username,
            "password": password
        }

        setLoading(true);

        login(payload)
            .then(response => {
                setLoading(false);
                enqueueSnackbar("Login successful.", {variant: 'success'});
                localStorage.setItem(ACCESS_TOKEN, response.data.token);
                history.push("/dashboard");
            })
            .catch(function (error) {
                console.log(error);
                if (error.status === 403) {
                    enqueueSnackbar("Your Username or Password is incorrect. Please try again!", {variant: 'error'});
                } else {
                    setLoading(false);
                    enqueueSnackbar(error.message || 'Sorry! Something went wrong. Please try again!', {variant: 'error'});
                }
            });
    }

    return (
        <Container component="main" maxWidth="xs">
            <CssBaseline/>
            <div className={classes.paper}>
                <Avatar className={classes.avatar}>
                    <LockOutlinedIcon/>
                </Avatar>
                <Typography component="h1" variant="h5">
                    Sign in
                </Typography>
                <form className={classes.form} onSubmit={handleSubmit}>
                    <TextField
                        variant="outlined"
                        margin="normal"
                        required
                        fullWidth
                        id="username"
                        label="Username"
                        name="username"
                        autoComplete="username"
                        autoFocus
                        onChange={e => setUsername(e.target.value)}
                    />
                    <TextField
                        variant="outlined"
                        margin="normal"
                        required
                        fullWidth
                        name="password"
                        label="Password"
                        type="password"
                        id="password"
                        autoComplete="current-password"
                        onChange={e => setPassword(e.target.value)}
                    />
                    <FormControlLabel
                        control={<Checkbox value="remember" color="primary"/>}
                        label="Remember me"
                    />
                    <Button
                        type="submit"
                        fullWidth
                        variant="contained"
                        color="primary"
                        className={classes.submit}
                        disabled={loading}
                    >
                        {loading && <CircularProgress size={14}/>}
                        Sign In
                    </Button>
                    <Grid container>
                        <Grid item xs>
                            <Link href="#" variant="body2">
                                Forgot password?
                            </Link>
                        </Grid>
                        <Grid item>
                            <Link href="/signup" variant="body2">
                                {"Don't have an account? Sign Up"}
                            </Link>
                        </Grid>
                    </Grid>
                </form>
            </div>
            <Footer/>
        </Container>
    );
}