import React from "react";
import AppBar from "@material-ui/core/AppBar";
import Toolbar from "@material-ui/core/Toolbar";
import ArrowBackIcon from "@material-ui/icons/ArrowBack";
import IconButton from "@material-ui/core/IconButton";
import useScrollTrigger from "@material-ui/core/useScrollTrigger";
import ExitToAppIcon from "@material-ui/icons/ExitToApp";
import toolBarIndexStyle from "../styles/toolBarIndexStyle";
import Typography from "@material-ui/core/Typography";
import { Grid } from "@material-ui/core";
import Link from "next/link";
import auth from "../Auth";

function ElevationScroll(props) {
  const { children } = props;
  const trigger = useScrollTrigger({
    disableHysteresis: true,
    threshold: 0
  });

  return React.cloneElement(children, {
    elevation: trigger ? 1 : 0
  });
}

export default function EventToolBar(props) {
  const { name } = props;
  return (
    <ElevationScroll>
      <AppBar color="secondary">
        <Toolbar>
          <Grid
            container
            direction="row"
            justify="space-between"
            alignItems="center"
            style={{ width: "100%" }}
          >
            <Grid item xs={2}>
              <Link href="/">
                <IconButton>
                  <ArrowBackIcon />
                </IconButton>
              </Link>
            </Grid>
            <Grid container justify="center" xs={8}>
              <Grid item xs={8}>
                <Typography variant="h6" color="primary" align="center">
                  {name}
                </Typography>
              </Grid>
            </Grid>
            <Grid container justify="flex-end" xs={2}>
              <Grid item xs={2} class={toolBarIndexStyle}>
                <Link href="/">
                  <IconButton onClick={() => auth.logout()} align="right">
                    <ExitToAppIcon />
                  </IconButton>
                </Link>
              </Grid>
            </Grid>
          </Grid>
        </Toolbar>
      </AppBar>
    </ElevationScroll>
  );
}
