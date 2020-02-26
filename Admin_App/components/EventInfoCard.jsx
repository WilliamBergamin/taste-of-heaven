import React from "react";
import { makeStyles } from "@material-ui/core/styles";
import Card from "@material-ui/core/Card";
import CardActions from "@material-ui/core/CardActions";
import CardContent from "@material-ui/core/CardContent";
import Button from "@material-ui/core/Button";
import Typography from "@material-ui/core/Typography";

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
        <Button variant="outlined" size="small" color="primary">
          QR code
        </Button>
      </CardActions>
    </Card>
  );
}
