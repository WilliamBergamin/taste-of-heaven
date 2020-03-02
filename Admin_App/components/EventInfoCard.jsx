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
import { Grid } from "@material-ui/core";
import QRCode from "qrcode.react";

const useStyles = makeStyles({
  root: {
    minWidth: 275
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

export default function EventInfoCard(props) {
  const classes = useStyles();
  const { name } = props;
  const { eventKey } = props;
  const { location } = props;
  const [open, setOpen] = React.useState(false);

  const handleClickOpen = () => {
    setOpen(true);
  };

  const handleClose = () => {
    setOpen(false);
  };

  return (
    <Card className={classes.root} variant="outlined">
      <CardContent>
        <Typography variant="h5" component="h2">
          {name}
        </Typography>
        <Typography className={classes.pos} color="textSecondary">
          {eventKey}
        </Typography>
        <Typography color="textSecondary" gutterBottom>
          Location:
        </Typography>
        <Typography variant="body2" component="p">
          {location}
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
          <QRCode id={eventKey} value={eventKey} level={"H"} size={200} />
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
