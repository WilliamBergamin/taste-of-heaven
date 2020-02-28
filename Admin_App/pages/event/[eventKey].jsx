import React from "react";
// eslint-disable-next-line no-unused-vars
import fetch from "isomorphic-unfetch";
import Cookies from "universal-cookie";
import { Grid } from "@material-ui/core";
import EventToolBar from "../../components/EventToolBar";
import auth from "../../Auth";
import YouMustLogIn from "../../components/YouMustLogIn";
import ListItem from "@material-ui/core/ListItem";
import EventInfoCard from "../../components/EventInfoCard";
import Router from "next/router";
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
    fetch(baseURL + "/api/v1/event/" + Router.query.eventKey, {
      method: "GET",
      headers: {
        Authorization: "Token " + auth.getIdToken()
      }
    })
      .then(res => res.json())
      .then(json =>
        this.state = {
          event: json,
          machines: []
        }
      );
    for (let i = 0; i < this.props.event.length; i = +1) {
      fetch(baseURL + "/api/v1/machine/" + this.state.event.machines[i], {
        method: "GET",
        headers: {
          Authorization: "Token " + auth.getIdToken()
        }
      })
        .then(res => res.json())
        .then(json =>
          this.setState({ machines: this.state.machines.append(json) })
        );
    }
  }

  // static async getInitialProps(ctx) {
  //   return { eventKey: JSON.parse(ctx.query.eventKey) };
  // }

  render() {
    const { event } = this.state;
    const { machines } = this.state;
    console.log(event);
    console.log(machines);
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
