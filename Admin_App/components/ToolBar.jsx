import React from "react";
import AppBar from "@material-ui/core/AppBar";
import Toolbar from "@material-ui/core/Toolbar";
import IconButton from "@material-ui/core/IconButton";
import LocalBarIcon from "@material-ui/icons/LocalBar";
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

export default function ToolBar(props) {
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
            <Grid item xs={4}>
              <Link href="/">
                <IconButton>
                  <LocalBarIcon />
                </IconButton>
              </Link>
            </Grid>
            <Grid container justify="center" xs={4}>
              <Grid item xs={4} class={toolBarIndexStyle.centerItem}>
                <Typography variant="h6" color="primary" align="center">
                  Events
                </Typography>
              </Grid>
            </Grid>
            <Grid container justify="flex-end" xs={4}>
              <Grid item xs={4} class={toolBarIndexStyle.rightItem}>
                <Link href="/">
                  <IconButton onClick={() => auth.logout()}>
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
