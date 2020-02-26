import React from "react";
// eslint-disable-next-line no-unused-vars
import fetch from "isomorphic-unfetch";
import Cookies from "universal-cookie";
import { Grid } from "@material-ui/core";
import EventToolBar from "../../components/EventToolBar";
import auth from "../../Auth";
import YouMustLogIn from "../../components/YouMustLogIn";
import ListItem from "@material-ui/core/ListItem";
import EventInfoCard from "../../components/EventInfoCard"
import useListStyle from "../../styles/eventSearchStyle";
import Paper from "@material-ui/core/Paper";

const baseURL = "http://3.133.81.46:80";

function ListItemLink(props) {
  return <ListItem button component="a" {...props} />;
}

class event extends React.Component {
  constructor(props) {
    super(props);
    auth.initialise(props);
    this.state = {
      event: this.props.event
    };
    //   const cookies = new Cookies(req.cookies);
  }

  static async getInitialProps(ctx) {
    return { event: JSON.parse(ctx.query.event) };
  }

  render() {
    const { event } = this.state;
    console.log(event);
    return !auth.isAuthenticated() ? (
      <YouMustLogIn />
    ) : (
      <Grid
        container
        justify="center"
        style={{ width: "100%", marginTop: "5rem" }}
      >
        <EventToolBar name={event.name} />
        <EventInfoCard
          name={event.name}
          eventKey={event.event_key}
          location={event.location}
        />
      </Grid>
    );
  }
}

export default event;
