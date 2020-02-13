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


class Login extends React.Component {
  constructor(props) {
    super(props);
    this.cursorInput = React.createRef();
    this.state = {
      numberOrder: 0,
    };
  }

  render() {
    const { numberOrder } = this.state;
    return (
      <ThemeProvider theme={theme}>
      <Zoom in timeout={{ enter: 1500 }}>
        <Grid container justify="center" style={{ width: '100%', marginTop: '20%' }}>
          <Paper style={useIndexStyle.paper}>
            <Grid container justify="center" direction="column" alignItems="center">
              {
                (false)
                  ? (
                    <Link href="/p/[cursor]?numbero=10" as={`/p/${0}?numbero=${numberOrder}`}>
                      <Button
                        style={{ marginTop: '10%' }}
                        variant="outlined"
                        size="small"
                        color="primary"
                      >
                        Start
                   </Button>
                    </Link>
                  )
                  : (
                    <Button
                      style={{ marginTop: '10%' }}
                      variant="outlined"
                      size="small"
                      color="primary"
                      onClick={() => auth.login()}
                    >
                      Log In
                 </Button>
                  )
              }
            </Grid>
          </Paper>
        </Grid>
      </Zoom>
      </ThemeProvider>
    );
  }
}
export default Login;
