import React from 'react';
import AppBar from '@material-ui/core/AppBar';
import Toolbar from '@material-ui/core/Toolbar';
import Button from '@material-ui/core/Button';
import TextField from '@material-ui/core/TextField';
import SearchIcon from '@material-ui/icons/Search';
import IconButton from '@material-ui/core/IconButton';
import useScrollTrigger from '@material-ui/core/useScrollTrigger';
import ExitToAppIcon from '@material-ui/icons/ExitToApp';
import useIndexStyle from '../styles/IndexStyle';
import { Grid } from '@material-ui/core';
import Link from 'next/link';
import auth from '../Auth';


function ElevationScroll(props) {
  const { children } = props;
  const trigger = useScrollTrigger({
    disableHysteresis: true,
    threshold: 0,
  });

  return React.cloneElement(children, {
    elevation: trigger ? 1 : 0,
  });
}

export default function ToolBar(props) {

  return (
    <ElevationScroll>
      <AppBar color="secondary">
        <Toolbar>
          <Grid container direction="row-reverse" style={{ width: '100%' }}>
            <Link href="/">
              <IconButton
                onClick={() => auth.logout()}
              >
                <ExitToAppIcon />
              </IconButton>
            </Link>
          </Grid>
        </Toolbar>
      </AppBar>
    </ElevationScroll>
  );
}
