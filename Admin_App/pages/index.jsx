import React from 'react'
import Head from 'next/head'
import Link from 'next/link';
// eslint-disable-next-line no-unused-vars
import fetch from 'isomorphic-unfetch';
import Paper from '@material-ui/core/Paper';
import Button from '@material-ui/core/Button';
import { ThemeProvider } from '@material-ui/styles';
import theme from '../theme/Theme'
import Zoom from '@material-ui/core/Zoom';
import { Grid } from '@material-ui/core';
import useIndexStyle from '../styles/IndexStyle';
import TextField from '@material-ui/core/TextField';
import auth from '../Auth';

class Login extends React.Component {
  constructor(props) {
    super(props);
    auth.initialise(props);
    this.cursorInput = React.createRef();
    this.state = {
      password: "",
      emial: "",
    };
  }

  handleChangePass = event => {
    this.state.password = event.target.value;
  };

  handleChangeEmail = event => {
    this.state.password = event.target.value;
  };

  render() {
    const { password, email } = this.state;
    return (
      <Zoom in timeout={{ enter: 1500 }}>
        <Grid container justify="center" style={{ width: '100%', marginTop: '20%' }}>
          <Paper style={useIndexStyle.paper}>
            <Grid container justify="center" direction="column" alignItems="center">
              {
                (auth.isAuthenticated())
                  ? (
                    <Link href="/p/[cursor]?numbero=10" as={`/p/${0}?numbero=${numberOrder}`}>
                      <Button
                        style={{ marginTop: '10%' }}
                        variant="outlined"
                        size="small"
                        color="secondary"
                      >
                        Start
                   </Button>
                    </Link>
                  )
                  : (
                    <Grid container justify="center" direction="column" alignItems="center">
                      <Grid item>
                        <TextField
                          style={{ marginTop: '3%', marginBottom: '3%' }}
                          id="outlined-password-input"
                          label="Password"
                          type="password"
                          autoComplete="current-password"
                          variant="outlined"
                          color="white"
                          value={password}
                          onChange={this.handleChangePass}
                        />
                      </Grid>
                      <Grid item>
                        <TextField
                          style={{ marginTop: '3%', marginBottom: '3%' }}
                          color="primary"
                          id="outlined-search"
                          label="Email"
                          type="search"
                          variant="outlined" />
                      </Grid>
                      <Grid item>
                        <Button
                          style={{ marginTop: '10%' }}
                          variant="outlined"
                          size="small"
                          color="primary"
                          onClick={() => auth.login()}
                        >
                          Log In
                 </Button>
                      </Grid>
                    </Grid>

                  )
              }
            </Grid>
          </Paper>
        </Grid>
      </Zoom>
    );
  }
}
export default Login;
