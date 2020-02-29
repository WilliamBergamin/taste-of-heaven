import React from "react";
// eslint-disable-next-line no-unused-vars
import fetch from "isomorphic-unfetch";
import { Grid } from "@material-ui/core";
import Link from "next/link";
import Button from "@material-ui/core/Button";
import Paper from "@material-ui/core/Paper";
import useIndexStyle from "../styles/IndexStyle";

export default function YouMustLogIn() {
  return (
    <Grid
      container
      justify="center"
      style={{ width: "100%", marginTop: "20%" }}
    >
      <Paper style={useIndexStyle.paper}>
        <Grid container justify="center" direction="column" alignItems="center">
          <Link href="/">
            <Button
              variant="outlined"
              size="small"
              color="primary"
              style={{ margin: "0.5rem" }}
            >
              You Must Log In
            </Button>
          </Link>
        </Grid>
      </Paper>
    </Grid>
  );
}
