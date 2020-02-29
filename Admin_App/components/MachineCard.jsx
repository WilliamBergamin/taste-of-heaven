import React from "react";
import { makeStyles } from "@material-ui/core/styles";
import Card from "@material-ui/core/Card";
import CardActions from "@material-ui/core/CardActions";
import CardContent from "@material-ui/core/CardContent";
import Button from "@material-ui/core/Button";
import Typography from "@material-ui/core/Typography";
import Dialog from "@material-ui/core/Dialog";
import DialogActions from "@material-ui/core/DialogActions";
import DialogContent from "@material-ui/core/DialogContent";
import QRCode from "qrcode.react";
import { Grid } from "@material-ui/core";

const useStyles = makeStyles({
  root: {
    minWidth: 275
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
  const [open, setOpen] = React.useState(false);

  const handleClickOpen = () => {
    setOpen(true);
  };

  const handleClose = () => {
    setOpen(false);
  };

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
        <Grid
          container
          justify="center"
          direction="row-reverse"
          justify="flex-start"
        >
          <Button
            variant="outlined"
            size="small"
            color="primary"
            onClick={handleClickOpen}
          >
            QR code
          </Button>
        </Grid>
      </CardActions>
      <Dialog
        open={open}
        onClose={handleClose}
        aria-labelledby="form-dialog-title"
      >
        <DialogContent>
          <QRCode
            id={machine.machine_key}
            value={machine.machine_key}
            level={"H"}
            size={200}
          />
        </DialogContent>
        <DialogActions>
          <Button
            onClick={handleClose}
            variant="outlined"
            size="small"
            color="primary"
          >
            close
          </Button>
        </DialogActions>
      </Dialog>
    </Card>
  );
}
