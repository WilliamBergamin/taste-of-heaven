import React from "react";
import { makeStyles } from "@material-ui/core/styles";
import Card from "@material-ui/core/Card";
import CardActions from "@material-ui/core/CardActions";
import CardContent from "@material-ui/core/CardContent";
import Button from "@material-ui/core/Button";
import Typography from "@material-ui/core/Typography";
import { red, green } from "@material-ui/core/colors";

const useStyles = makeStyles({
  root: {
    minWidth: 275,
  },
  error: {
    minWidth: 275,
    background: "#FF0000"
  },
  bullet: {
    display: "inline-block",
    margin: "0 2px",
    transform: "scale(0.8)"
  },
  pos: {
    marginBottom: 12
  }
});

export default function MachineCard(props) {
  const classes = useStyles();
  const { machine } = props;

  return (
    <Card
      className={machine.state === "ok" ? classes.root : classes.error}
      variant="outlined"
    >
      <CardContent>
        <Typography color="textSecondary" gutterBottom>
          Machine
        </Typography>
        <Typography variant="h5" component="h2">
          {machine.state}
        </Typography>
        {machine.error == null ? null : (
          <Typography variant="body2" component="p">
            {machine.error}
          </Typography>
        )}
        <Typography className={classes.pos} color="textSecondary">
          {machine.machine_key}
        </Typography>
      </CardContent>
      <CardActions>
        <Button variant="outlined" size="small" color="primary">
          QR code
        </Button>
      </CardActions>
    </Card>
  );
}
