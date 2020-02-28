import React from "react";
// eslint-disable-next-line no-unused-vars
import fetch from "isomorphic-unfetch";
import Cookies from "universal-cookie";
import { Grid } from "@material-ui/core";
import EventToolBar from "../../components/EventToolBar";
import auth from "../../Auth";
import YouMustLogIn from "../../components/YouMustLogIn";
import EventInfoCard from "../../components/EventInfoCard";
import MachineCard from "../../components/MachineCard";
import Fab from "@material-ui/core/Fab";
import AddIcon from "@material-ui/icons/Add";
import useFabStyle from "../../styles/fabStyle";
import { withRouter } from "next/router";

const baseURL = "http://3.133.81.46:80";

class event extends React.Component {
  constructor(props) {
    super(props);
    auth.initialise(props);
    const { router } = this.props;
    const cookies = new Cookies(props.cookies);
    let eventKey = router.query.eventKey;
    if (eventKey == null) {
      eventKey = cookies.get("lastEventKey");
    } else {
      cookies.set("lastEventKey", router.query.eventKey);
    }
    this.state = {
      event: {},
      machines: []
    };
    fetch(baseURL + "/api/v1/event/" + eventKey, {
      method: "GET",
      headers: {
        Authorization: "Token " + auth.getIdToken()
      }
    })
      .then(res => res.json())
      .then(json => {
        for (let i = 0; i < json.machines.length; i = +1) {
          fetch(baseURL + "/api/v1/machine/" + json.machines[i], {
            method: "GET",
            headers: {
              Authorization: "Token " + auth.getIdToken()
            }
          })
            .then(res => res.json())
            .then(json =>
              this.setState({ machines: this.state.machines.concat(json) })
            );
        }
        this.setState({ event: json });
      });
  }

  render() {
    const { event } = this.state;
    const { machines } = this.state;
    console.log(machines);
    return !auth.isAuthenticated() ? (
      <YouMustLogIn />
    ) : (
      <Grid
        container
        justify="center"
        spacing={2}
        style={{ marginTop: "5rem" }}
      >
        <EventToolBar name={event.name} />
        <Grid item key={event.eventKey} xs={11} lg={4}>
          <EventInfoCard
            name={event.name}
            eventKey={event.event_key}
            location={event.location}
          />
        </Grid>
        {machines.map(machine => (
          <Grid item key={machine.machine_key} xs={11} lg={4}>
            <MachineCard machine={machine} />
          </Grid>
        ))}
        <Fab color="secondary" style={useFabStyle.fab} aria-label="add">
          <AddIcon />
        </Fab>
      </Grid>
    );
  }
}

export default withRouter(event);
