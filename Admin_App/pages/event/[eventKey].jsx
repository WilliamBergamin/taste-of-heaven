import React from "react";
import fetch from "isomorphic-unfetch";
import Cookies from "universal-cookie";
import { Grid } from "@material-ui/core";
import { withRouter } from "next/router";
import EventToolBar from "../../components/EventToolBar";
import auth from "../../Auth";
import YouMustLogIn from "../../components/YouMustLogIn";
import EventInfoCard from "../../components/EventInfoCard";
import MachineCard from "../../components/MachineCard";
import FormDialogFabMachine from "../../components/FormDialogFabMachine";

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
      event: { machines: [] },
      eventKey: eventKey
    };
  }

  componentDidMount() {
    fetch(process.env.SERVER_BASE_URL + "/api/v1/event/" + this.state.eventKey, {
      method: "GET",
      headers: {
        Authorization: "Token " + auth.getIdToken()
      }
    })
      .then(res => res.json())
      .then(json => {
        this.setState({ event: json });
      });
  }

  handleNewMachine = result => {
    const newEvent = this.state.event;
    newEvent.machines.push(result.machine_key);
    this.setState({
      event: newEvent
    });
  };

  handleCreateMachine = () => {
    const eventKey = this.state.eventKey;
    return new Promise(async (resolve, reject) => {
      const response = await fetch(process.env.SERVER_BASE_URL + "/api/v1/machine/" + eventKey, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          Authorization: "Token " + auth.getIdToken()
        }
      });
      const responseJson = await response.json();
      if (!responseJson || !responseJson.token) {
        return reject();
      }
      return resolve(responseJson);
    });
  };

  render() {
    const { event } = this.state;
    return !auth.isAuthenticated() ? (
      <YouMustLogIn />
    ) : (
      <div>
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
          {event.machines.map(machineKey => (
            <Grid item key={machineKey} xs={11} lg={4}>
              <MachineCard machineKey={machineKey} auth={auth} />
            </Grid>
          ))}
        </Grid>
        <FormDialogFabMachine
          executeCreate={this.handleCreateMachine}
          handleNewMachine={this.handleNewMachine}
        />
      </div>
    );
  }
}

export default withRouter(event);
