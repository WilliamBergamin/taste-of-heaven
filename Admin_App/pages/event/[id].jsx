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
    for(let i=0; i<this.props.event.length; i=+1){
      new Promis(resolve => {
        fetch(baseURL + "/api/v1/machine/" + this.props.event.machines[i], {
          method: "GET",
          headers: {
            Authorization: "Token " + auth.getIdToken()
          }
        })
          .then(res => res.json())
          .then(json => this.setState({ machines: this.state.machines.append(json) }));
        resolve();
      });
    }
  }

  state = {
    machines: []
  };

  static async getInitialProps(ctx) {
    return { event: JSON.parse(ctx.query.event) };
  }

  // componentDidMount() {
  //   fetch(baseURL + "/api/v1/events", {
  //     method: "GET",
  //     headers: {
  //       Authorization: "Token " + auth.getIdToken()
  //     }
  //   })
  //     .then(res => res.json())
  //     .then(json => this.setState({ allEvents: json, events: json }));
  // }

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
