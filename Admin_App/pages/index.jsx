import React from "react";
import Paper from "@material-ui/core/Paper";
import Button from "@material-ui/core/Button";
import Zoom from "@material-ui/core/Zoom";
import { Grid } from "@material-ui/core";
import Alert from "@material-ui/lab/Alert";
import Snackbar from "@material-ui/core/Snackbar";
import TextField from "@material-ui/core/TextField";
import Router from "next/router";
import useIndexStyle from "../styles/IndexStyle";
import auth from "../Auth";

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

  handleAuthenticated = () => {
    Router.push("/events");
    return null;
  };

  render() {
    const { password, email, open } = this.state;
    return auth.isAuthenticated() ?
      (this.handleAuthenticated()) : (
        <div style={{display: 'flex',  justifyContent:'center', alignItems:'center', height: '100vh'}}>
          <Zoom in timeout={{ enter: 1500 }}>
            <Grid
              container
              justify="center"
            >
              <Grid item lg={2} md={4} sm={6} xs={10}>
                <Paper style={useIndexStyle.paper}>
                <img
                src="/static/logo.png"
                width="200"
                height="auto"
                alt=""
                style={{display: "block",
                  marginLeft: "auto",
                  marginRight: "auto",
                  width: "50%;"}}
              />
                      <TextField
                        style={{ marginTop: "3%", marginBottom: "3%" }}
                        id="outlined"
                        label="Email"
                        type="email"
                        fullWidth
                        value={email}
                        onChange={this.handleChangeEmail}
                      />
                      <TextField
                        style={{ marginTop: "3%", marginBottom: "3%"}}
                        id="outlined-password-input"
                        label="Password"
                        type="password"
                        autoComplete="current-password"
                        fullWidth
                        value={password}
                        onChange={this.handleChangePass}
                        onKeyDown={e => {
                          if (e.keyCode === 13) {
                            auth.handleAuthentication(email, password).then(
                              () => {
                                Router.push("/events");
                              },
                              () => {
                                this.handleError();
                              }
                            );
                          }
                        }}
                      />
                  <Grid
                    container
                    justify="center"
                    direction="column"
                    alignItems="center"
                  >
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
                    </Grid>
                  </Grid>
                </Paper>
              </Grid>
            </Grid>
          </Zoom>
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
        </div>);

  }
}
export default Login;
