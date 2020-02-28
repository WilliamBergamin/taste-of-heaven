import React from "react";
// eslint-disable-next-line no-unused-vars
import fetch from "isomorphic-unfetch";
import Paper from "@material-ui/core/Paper";
import Button from "@material-ui/core/Button";
import Zoom from "@material-ui/core/Zoom";
import { Grid } from "@material-ui/core";
import Alert from "@material-ui/lab/Alert";
import useIndexStyle from "../styles/IndexStyle";
import TextField from "@material-ui/core/TextField";
import auth from "../Auth";
import Router from "next/router";
import Snackbar from "@material-ui/core/Snackbar";

class Login extends React.Component {
  constructor(props) {
    super(props);
    auth.initialise(props);
    this.cursorInput = React.createRef();
  }

  state = {
    password: "",
    email: "",
    open: false
  };

  handleError = () => {
    this.setState({
      open: true
    });
  };

  handleChangePass = event => {
    this.setState({
      password: event.target.value
    });
  };

  handleChangeEmail = event => {
    this.setState({
      email: event.target.value
    });
  };

  handleClose = (event, reason) => {
    if (reason === "clickaway") {
      return;
    }
    this.setState({
      open: false
    });
  };

  render() {
    const { password, email, open } = this.state;
    return (
      <Zoom in timeout={{ enter: 1500 }}>
        <Grid
          container
          justify="center"
          style={{ width: "100%", marginTop: "20%" }}
        >
          <Paper style={useIndexStyle.paper}>
            <Grid
              container
              justify="center"
              direction="column"
              alignItems="center"
            >
              {auth.isAuthenticated() ? (
                Router.push("/events")
              ) : (
                <Grid
                  container
                  justify="center"
                  direction="column"
                  alignItems="center"
                >
                  <Grid item>
                    <TextField
                      style={{ marginTop: "3%", marginBottom: "3%" }}
                      id="outlined"
                      label="Email"
                      type="email"
                      variant="outlined"
                      value={email}
                      onChange={this.handleChangeEmail}
                    />
                  </Grid>
                  <Grid item>
                    <TextField
                      style={{ marginTop: "3%", marginBottom: "3%" }}
                      id="outlined-password-input"
                      label="Password"
                      type="password"
                      autoComplete="current-password"
                      variant="outlined"
                      value={password}
                      onChange={this.handleChangePass}
                    />
                  </Grid>
                  <Grid item>
                    <Button
                      style={{ marginTop: "10%" }}
                      variant="outlined"
                      size="small"
                      color="primary"
                      onClick={() => {
                        auth.handleAuthentication(email, password).then(
                          () => {
                            Router.push("/events");
                          },
                          () => {
                            this.handleError();
                          }
                        );
                      }}
                    >
                      Log In
                    </Button>
                    <Snackbar
                      open={open}
                      autoHideDuration={3000}
                      onClose={this.handleClose}
                    >
                      <Alert
                        variant="filled"
                        onClose={this.handleClose}
                        severity="warning"
                      >
                        Email or Password not valid!
                      </Alert>
                    </Snackbar>
                  </Grid>
                </Grid>
              )}
            </Grid>
          </Paper>
        </Grid>
      </Zoom>
    );
  }
}
export default Login;
