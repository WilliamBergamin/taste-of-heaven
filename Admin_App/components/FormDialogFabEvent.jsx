import React from "react";
import Button from "@material-ui/core/Button";
import TextField from "@material-ui/core/TextField";
import Dialog from "@material-ui/core/Dialog";
import DialogActions from "@material-ui/core/DialogActions";
import DialogContent from "@material-ui/core/DialogContent";
import DialogContentText from "@material-ui/core/DialogContentText";
import DialogTitle from "@material-ui/core/DialogTitle";
import Fab from "@material-ui/core/Fab";
import AddIcon from "@material-ui/icons/Add";
import useFabStyle from "../styles/fabStyle";
import Snackbar from "@material-ui/core/Snackbar";
import Alert from "@material-ui/lab/Alert";

export default function FormDialogFab(props) {
  const { executeCreate, handleNewEvent } = props;
  const [open, setOpen] = React.useState(false);
  const [error, setError] = React.useState(false);
  const [name, setName] = React.useState("");
  const [location, setLocation] = React.useState("");

  const handleClickOpen = () => {
    setOpen(true);
  };

  const handleClose = () => {
    setOpen(false);
  };

  const handleErrorClose = (event, reason) => {
    if (reason === "clickaway") {
      return;
    }
    setError(false);
  };

  const handleCreate = () => {
    executeCreate(name, location).then(
      result => {
        handleNewEvent(result);
        setOpen(false);
      },
      error => {
        setError(true);
      }
    );
  };

  return (
    <div>
      <Fab
        color="secondary"
        style={useFabStyle.fab}
        aria-label="add"
        onClick={handleClickOpen}
      >
        <AddIcon color="primary" />
      </Fab>
      <Dialog
        open={open}
        onClose={handleClose}
        aria-labelledby="form-dialog-title"
      >
        <DialogTitle id="form-dialog-title">
          You are Creating a new machine!
        </DialogTitle>
        <DialogContent>
          <DialogContentText id="alert-dialog-description">
            Are you sure you want to create a new event, this action cannot be
            undone by a user with your priviledges?
          </DialogContentText>
          <TextField
            autoFocus
            margin="dense"
            id="name"
            label="Name"
            type="text"
            fullWidth
            onChange={event => {
              setName(event.target.value);
            }}
          />
          <TextField
            autoFocus
            margin="dense"
            id="location"
            label="Location"
            type="text"
            fullWidth
            onChange={event => {
              setLocation(event.target.value);
            }}
          />
        </DialogContent>
        <DialogActions>
          <Button
            onClick={handleClose}
            variant="outlined"
            size="small"
            color="primary"
          >
            Cancel
          </Button>
          <Button
            onClick={handleCreate}
            variant="outlined"
            size="small"
            color="primary"
          >
            Create
          </Button>
        </DialogActions>
      </Dialog>
      <Snackbar open={error} autoHideDuration={3000} onClose={handleErrorClose}>
        <Alert variant="filled" onClose={handleErrorClose} severity="warning">
          Error occured in event creation!
        </Alert>
      </Snackbar>
    </div>
  );
}
